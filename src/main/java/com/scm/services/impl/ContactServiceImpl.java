package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {

        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);

        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {

        var oldContact = contactRepo.findById(contact.getId()).orElseThrow(() -> new ResourceNotFoundException("Contact Not Found!"));
        
        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setPhoneNumber(contact.getPhoneNumber());
        oldContact.setAddress(contact.getAddress());
        oldContact.setSocialLink1(contact.getSocialLink1());
        oldContact.setSocialLink2(contact.getSocialLink2());
        oldContact.setFavourite(contact.isFavourite());
        oldContact.setContactPic(contact.getContactPic());
        oldContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(oldContact);
    }

    @Override
    public List<Contact> getAll() {
        
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact Not Found with ID : " + id));
    }

    @Override
    public void delete(String id) {
        
        var contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact Not Found with ID : " + id));

        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equals("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int page, int size, String sortBy, String sortOrder, User user) {

        Sort sort = sortOrder.equals("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int page, int size, String sortBy, String sortOrder, User user) {
        Sort sort = sortOrder.equals("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortBy, String sortOrder, User user) {
                
        Sort sort = sortOrder.equals("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        
        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

}
