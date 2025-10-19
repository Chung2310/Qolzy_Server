package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.contact.ContactResponse;
import com.example.Qolzy.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/contact")
@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContactResponse>>> getContacts(@RequestParam Long userId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size){
        return contactService.getContacts(userId, page, size);
    }
}
