package com.identity_reconciliation.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.identity_reconciliation.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByEmail(String email);

    List<Contact> findByPhoneNumber(String phoneNumber);
}
