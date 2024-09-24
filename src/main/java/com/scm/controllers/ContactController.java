package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.scm.entities.Contact;
import com.scm.entities.Message;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;
    
    // Add Contact Page : Handler.. 
    @RequestMapping("/add")
    public String addContactView(Model model) {
        
        // This Empty Obj set to model can be accessed in add_contact.html and after form submitted all the data got stored in this Obj can be accessed here.
        model.addAttribute("contactForm", new ContactForm());
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult, Authentication authentication, HttpSession session) {
     // Process the Form Data....

        // Validate Form...
        if(rBindingResult.hasErrors()) {
            rBindingResult.getAllErrors().forEach(error -> logger.info(error.toString()));
            // Set Error Message to be Displayed in View...
            Message message = Message.builder()
                                    .content("Please Correct the following Errors!")
                                    .type(MessageType.red)
                                    .build();

            session.setAttribute("message", message);

            return "user/add_contact";
        }

        // Fetching user to store in DB with contact details...
        String username = Helper.getEmailOfLoggedUser(authentication);
        User user = userService.getUserByEmail(username);

        // (Data : Form --> contact obj)...
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setSocialLink1(contactForm.getSocialLink1());
        contact.setSocialLink2(contactForm.getSocialLink2());
        contact.setFavourite(contactForm.isFavourite());
        contact.setUser(user);

        // Image Processing...
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String fileName = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setContactPic(fileURL);
            contact.setCloudinaryImagePublicId(fileName);
        }
        
        contactService.save(contact);

        System.out.println(contactForm);

        // Set Success Message to be Displayed in View...
        Message message = Message.builder()
                                .content("Contact Added Successfully!")
                                .type(MessageType.green)
                                .build();

        session.setAttribute("message", message);
        
        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContact(Model model, Authentication authentication,
     // NOTE:- @RequestParam() help us to get the parameter from webpage(user) and put it in the variable assign here...
        @RequestParam(value = "page", defaultValue = "0") int page, 
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size, 
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {

        // Loading all the Contacts Saved by the Particular User...
        String username = Helper.getEmailOfLoggedUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageOfContacts = contactService.getByUser(user, page, size, sortBy, sortOrder);

        model.addAttribute("pageOfContacts", pageOfContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        
        return "user/contacts";
    }

    // Search Handler... 
    @RequestMapping("/search")
    public String searchHeandler(Model model, Authentication authentication, 
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {

        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getKeyword());

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedUser(authentication));

        Page<Contact> pageOfContacts = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageOfContacts = contactService.searchByName(contactSearchForm.getKeyword(), page, size, sortBy, sortOrder, user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageOfContacts = contactService.searchByEmail(contactSearchForm.getKeyword(), page, size, sortBy, sortOrder, user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")) {
            pageOfContacts = contactService.searchByPhoneNumber(contactSearchForm.getKeyword(), page, size, sortBy, sortOrder, user);
        }

        logger.info("pageOfContacts {}", pageOfContacts);

        model.addAttribute("pageOfContacts", pageOfContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", contactSearchForm);

        return "user/search";
    }

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, HttpSession httpSession) {

        // Deleting Contact Image...
        var contact = contactService.getById(contactId);
        if(contact.getCloudinaryImagePublicId() != null && !contact.getCloudinaryImagePublicId().isEmpty()) {
            try {
                imageService.deleteImage(contact.getCloudinaryImagePublicId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Deleting Contact...
        contactService.delete(contactId);

        httpSession.setAttribute("message", Message.builder()
                                                .content("Contact Deleted Successfully!!")
                                                .type(MessageType.green)
                                                .build());        

        logger.info("Contact with ID : {} Deleted!",contactId);

        return "redirect:/user/contacts";
    }

    // UpdateView (Form)... 
    @GetMapping("/update/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model) {

        var contact = contactService.getById(contactId);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setSocialLink1(contact.getSocialLink1());
        contactForm.setSocialLink2(contact.getSocialLink2());
        contactForm.setContactPic(contact.getContactPic());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }

    // Updating Contact Data... 
    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId, Model model, HttpSession session, 
        @Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult) {

        if(rBindingResult.hasErrors()) {
            // Set Error Message to be Displayed in View...
            Message message = Message.builder()
                                    .content("Please Correct the following Errors!")
                                    .type(MessageType.red)
                                    .build();

            session.setAttribute("message", message);

            return "user/update_contact_view";
        }

        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setSocialLink1(contactForm.getSocialLink1());
        contact.setSocialLink2(contactForm.getSocialLink2());
        contact.setFavourite(contactForm.isFavourite());

        // Processing Image...
        var oldContact = contactService.getById(contactId);
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            // Deleting Old Image...
            if(oldContact.getCloudinaryImagePublicId() != null && !oldContact.getCloudinaryImagePublicId().isEmpty()) {
                try {
                    imageService.deleteImage(oldContact.getCloudinaryImagePublicId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Uploading New Image...
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setContactPic(imageUrl);
            contactForm.setContactPic(imageUrl);
        } 
        else {
            // Reinstating old Image...
            contact.setContactPic(oldContact.getContactPic());
            contact.setCloudinaryImagePublicId(oldContact.getCloudinaryImagePublicId());
        }
        
        var updatedContact = contactService.update(contact);

        logger.info("Updated Contact : {}", updatedContact);

        session.setAttribute("message", Message.builder()
                                                    .content("Contact Updated SuccessFully!")
                                                    .type(MessageType.green)
                                                    .build());
        
        return "redirect:/user/contacts";

    }
    
    
}
