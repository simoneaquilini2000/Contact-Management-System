package com.app.contactmanagementsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.contactmanagementsystem.controller.dto.ContactAdminDTO;
import com.app.contactmanagementsystem.controller.dto.ContactCreationDTO;
import com.app.contactmanagementsystem.controller.dto.ContactResponseDTO;
import com.app.contactmanagementsystem.exceptions.ContactNotFoundException;
import com.app.contactmanagementsystem.mapper.ContactMapper;
import com.app.contactmanagementsystem.repository.ContactsRepository;
import com.app.contactmanagementsystem.repository.model.ContactEntity;
import com.app.contactmanagementsystem.service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactsRepository contactRepository;

    private final ContactMapper contactMapper;

    private final UserService userService;

    public Page<ContactAdminDTO> getUserContacts(int page, int size) {
        log.info("Fetching contacts for user with pagination: page {}, size {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ContactEntity> contactPage = contactRepository.findAll(pageable);

        List<ContactAdminDTO> contactDTOs = contactPage.stream()
                .map(contactMapper::toAdminDto)
                .collect(Collectors.toList());

        return new PageImpl<>(contactDTOs, pageable, contactPage.getTotalElements());
    }

    public List<ContactResponseDTO> getUserContacts() {
        log.info("Fetching all contacts from the database");
        User user = userService.getCurrentUser();
        log.info("User found: {}", user);

        return contactRepository.findByUserId(user.getId()).stream()
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    public Long createContact(ContactCreationDTO contactDTO) {
        log.info("Creating a new contact: {}", contactDTO);
        // Implementation for creating a contact goes here
        User user = userService.getCurrentUser();

        ContactEntity contactEntity = contactMapper.toEntity(contactDTO);
        contactEntity.setUserId(user.getId());
        ContactEntity createdContact = contactRepository.save(contactEntity);

        return createdContact.getId();
    }

    public ContactResponseDTO getContactById(Long id) {
        log.info("Fetching all contacts from the database");
        User user = userService.getCurrentUser();
        log.info("User found: {}", user);

        return contactRepository.findByUserId(user.getId()).stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(contactMapper::toDto)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));
    }

    public ContactResponseDTO updateContact(Long id, ContactCreationDTO contactDTO) {
        log.info("Updating contact with id {}: {}", id, contactDTO);
        log.info("Fetching all contacts from the database");
        User user = userService.getCurrentUser();
        log.info("User found: {}", user);

        return contactRepository.findByUserId(user.getId()).stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(contact -> {
                    contact.setName(contactDTO.getName());
                    contact.setSurname(contactDTO.getSurname());
                    contact.setPhone(contactDTO.getPhone());
                    contactRepository.save(contact);
                    return contactMapper.toDto(contact);
                })
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));
    }

    public void deleteContact(Long id) {
        log.info("Deleting contact with id {}", id);
        ContactResponseDTO contact = getContactById(id);
        log.info("Contact to delete: {}", contact);

        contactRepository.deleteById(id);
    }
}
