package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.Message;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page Handler");

        // Sending data to view...
        model.addAttribute("name", "Substring Technologies");
        model.addAttribute("email", "kr.shiv112113@gmail.com");
        model.addAttribute("learning", "Spring Boot");
        model.addAttribute("github", "https://github.com/Shiv112113");
        return "home";
    }

    // About Page Router...
    @RequestMapping("/about")
    public String aboutPage() {
        System.out.println("About Page Loading");
        return "about";
    }

    // Service Page Router...
    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("Service Page Loading");
        return "services";
    }

    // ContactUs Page Router...
    @RequestMapping("/contactUs")
    public String contactUsPage() {
        System.out.println("ContactUs Page Loading");
        return "contactUs";
    }

    // Login Page Router...
    @RequestMapping("/login")
    public String loginPage() {
        System.out.println("Login Page Loading");
        return "login";
    }

    // Sign Up Page Router...
    @RequestMapping("/signup")
    public String signupPage() {
        System.out.println("Signup Page Loading");
        return "signup";
    }

    // The showSignupForm method initializes the userForm object and adds it to the model. 
    // This ensures that the userForm object is available when the signup.html template is rendered.
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "signup";
    }

    // Process Register...
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session) {
        System.out.println("Processing Registration");
        System.out.println(userForm);

        // Validate the Data
        if (rBindingResult.hasErrors()) {
            return "signup";
        }

        // UserForm --> User
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());

        User saveUser = userService.saveUser(user);

        System.out.println("User saved : " + saveUser);

        Message message = Message.builder()
                                .content("Registration Successful!")
                                .type(MessageType.green)
                                .build();

        session.setAttribute("message", message);

        return "redirect:/signup";
    }
}
