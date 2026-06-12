package com.app.contactmanagementsystem.mapper;

import org.springframework.stereotype.Component;

import com.app.contactmanagementsystem.controller.dto.ContactCreationDTO;
import com.app.contactmanagementsystem.controller.dto.ContactResponseDTO;
import com.app.contactmanagementsystem.repository.model.ContactEntity;

@Component
public class ContactMapper {

    public ContactResponseDTO toDto(ContactEntity contact) {
        if (contact == null) {
            return null;
        }
        ContactResponseDTO dto = new ContactResponseDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setSurname(contact.getSurname());
        return dto;
    }

    public ContactEntity toEntity(ContactCreationDTO contactDTO) {
        if (contactDTO == null) {
            return null;
        }
        ContactEntity entity = new ContactEntity();
        entity.setName(contactDTO.getName());
        entity.setSurname(contactDTO.getSurname());
        return entity;
    }
}
