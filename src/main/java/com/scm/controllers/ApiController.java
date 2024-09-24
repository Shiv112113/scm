package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.Contact;
import com.scm.services.ContactService;

/* @RestController :-
    1. This is a specialized version of the @Controller annotation combined with @ResponseBody. 
    2. This setup allows the controller to handle REST API requests such as GET, POST, DELETE, and PUT, 
        and return the response in JSON format directly to the client without rendering a view.
 */

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    ContactService contactService;

    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        return contactService.getById(contactId);

    }

}
