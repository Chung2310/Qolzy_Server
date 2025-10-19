package com.example.Qolzy.repository;

import com.example.Qolzy.model.contact.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndUserContactId(Long userId, Long userContactId);
    Contact findContactsByUserIdAndUserContactId(Long userId, Long userContactId);

}
