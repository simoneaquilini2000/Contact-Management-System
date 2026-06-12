package com.app.contactmanagementsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.contactmanagementsystem.controller.dto.ContactCreationDTO;
import com.app.contactmanagementsystem.controller.dto.ContactResponseDTO;
import com.app.contactmanagementsystem.mapper.ContactMapper;
import com.app.contactmanagementsystem.model.ContactEntity;
import com.app.contactmanagementsystem.model.UserEntity;
import com.app.contactmanagementsystem.repository.ContactsRepository;
import com.app.contactmanagementsystem.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactsRepository contactRepository;

    private final UsersRepository usersRepository;

    private final ContactMapper contactMapper;

    private final UserService userService;

    public List<ContactResponseDTO> getUserContacts() {
        log.info("Fetching all contacts from the database");
        UserEntity user = userService.getCurrentUser();
        log.info("User found: {}", user);

        return user.getContacts().stream().sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    public ContactResponseDTO createContact(ContactCreationDTO contactDTO) {
        log.info("Creating a new contact: {}", contactDTO);
        // Implementation for creating a contact goes here
        UserEntity user = userService.getCurrentUser();

        ContactEntity contactEntity = contactMapper.toEntity(contactDTO);
        user.getContacts().add(contactEntity);
        usersRepository.save(user);

        return contactMapper.toDto(contactEntity);
    }

    public ContactResponseDTO getContactById(Long id) {
        log.info("Fetching contact with id {}", id);
        UserEntity user = userService.getCurrentUser();
        log.info("User found: {}", user);

        return user.getContacts().stream()
                .filter(contact -> contact.getId().equals(id))
                .map(contactMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public ContactResponseDTO updateContact(Long id, ContactCreationDTO contactDTO) {
        log.info("Updating contact with id {}: {}", id, contactDTO);
        log.info("Fetching contact with id {}", id);
        UserEntity user = userService.getCurrentUser();
        log.info("User found: {}", user);

        ContactEntity contact = user.getContacts().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setName(contactDTO.getName());
        contact.setSurname(contactDTO.getSurname());
        usersRepository.save(user);

        return contactMapper.toDto(contact);
    }

    public void deleteContact(Long id) {
        log.info("Deleting contact with id {}", id);
        UserEntity user = userService.getCurrentUser();
        log.info("User found: {}", user);

        ContactEntity contact = user.getContacts().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        user.getContacts().remove(contact);
        usersRepository.save(user);
    }
}
