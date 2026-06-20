package com.app.contactmanagementsystem.controller;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.contactmanagementsystem.config.model.KeycloakRole;
import com.app.contactmanagementsystem.controller.dto.ContactAdminDTO;
import com.app.contactmanagementsystem.controller.dto.ContactCreationDTO;
import com.app.contactmanagementsystem.controller.dto.ContactResponseDTO;
import com.app.contactmanagementsystem.service.ContactService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Validated
@PreAuthorize("denyAll()")
public class ContactController {

    private final ContactService contactService;

    @PreAuthorize("hasRole('" + KeycloakRole.ADMIN + "')")
    @GetMapping("/paged")
    public ResponseEntity<Page<ContactAdminDTO>> getUserContactsPaged(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Fetching contacts for user with pagination: page {}, size {}", page, size);
        return ResponseEntity.ok(contactService.getUserContacts(page, size));
    }

    @GetMapping
    @PreAuthorize("hasRole('" + KeycloakRole.USER + "')")
    public ResponseEntity<ArrayList<ContactResponseDTO>> getAllContacts() {
        log.info("Fetching all contacts");
        return ResponseEntity.ok(new ArrayList<>(contactService.getUserContacts()));
    }

    @PostMapping
    @PreAuthorize("hasRole('" + KeycloakRole.USER + "')")
    public ResponseEntity<Long> createContact(
        @RequestBody @Validated ContactCreationDTO contactDTO
    ) {
        log.info("Creating a new contact: {}", contactDTO);
        Long newContactId = contactService.createContact(contactDTO);
        log.info("Created contact with id {}", newContactId);
        return ResponseEntity.ok(newContactId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + KeycloakRole.USER + "')")
    public ResponseEntity<ContactResponseDTO> updateContact(@PathVariable Long id, @RequestBody @Validated ContactCreationDTO contactDTO) {
        log.info("Updating contact with id {}: {}", id, contactDTO);
        ContactResponseDTO updatedContact = contactService.updateContact(id, contactDTO);
        return ResponseEntity.ok(updatedContact);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('" + KeycloakRole.USER + "')")
    public ResponseEntity<ContactResponseDTO> getContact(@PathVariable Long id) {
        log.info("Fetching contact with id {}", id);
        ContactResponseDTO contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + KeycloakRole.USER + "')")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        log.info("Deleted contact with id {}", id);
        return ResponseEntity.ok("Contact deleted successfully");
    }
}
