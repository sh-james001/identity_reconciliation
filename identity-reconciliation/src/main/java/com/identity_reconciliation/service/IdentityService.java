package com.identity_reconciliation.service;

import org.springframework.stereotype.Service;

import com.identity_reconciliation.entity.Contact;
import com.identity_reconciliation.entity.ContactInfo;
import com.identity_reconciliation.repo.ContactRepository;
import com.identity_reconciliation.reponse.IdentifyResponse;
import com.identity_reconciliation.request.IdentifyRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IdentityService {
    private final ContactRepository contactRepository;

    public IdentityService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public IdentifyResponse identifyContact(IdentifyRequest request) {
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        List<Contact> contacts = new ArrayList<>();
        if (email != null) {
            contacts.addAll(contactRepository.findByEmail(email));
        }
        if (phoneNumber != null) {
            contacts.addAll(contactRepository.findByPhoneNumber(phoneNumber));
        }

        if (contacts.isEmpty()) {
            // Create a new primary contact
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence("primary");
            newContact.setCreatedAt(LocalDateTime.now());
            Contact savedContact = contactRepository.save(newContact);

            // Return response with new primary contact
            return createResponse(savedContact, Collections.emptyList());
        } else {
            // Find primary contact
            Contact primaryContact = contacts.stream()
                    .filter(contact -> "primary".equals(contact.getLinkPrecedence()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Primary contact not found"));

            // Find secondary contacts
            List<Long> secondaryContactIds = contacts.stream()
                    .filter(contact -> "secondary".equals(contact.getLinkPrecedence()))
                    .map(Contact::getId)
                    .collect(Collectors.toList());

            // Return response with primary and secondary contacts
            return createResponse(primaryContact, secondaryContactIds);
        }
    }

    private IdentifyResponse createResponse(Contact primaryContact, List<Long> secondaryContactIds) {
        IdentifyResponse response = new IdentifyResponse();
        response.setContact(new ContactInfo(
                primaryContact.getId(),
                Arrays.asList(primaryContact.getEmail()),
                Arrays.asList(primaryContact.getPhoneNumber()),
                secondaryContactIds
        ));
        return response;
    }
}
