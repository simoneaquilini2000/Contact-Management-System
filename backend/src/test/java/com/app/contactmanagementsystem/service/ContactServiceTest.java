package com.app.contactmanagementsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.contactmanagementsystem.controller.dto.ContactCreationDTO;
import com.app.contactmanagementsystem.controller.dto.ContactResponseDTO;
import com.app.contactmanagementsystem.exceptions.ContactNotFoundException;
import com.app.contactmanagementsystem.mapper.ContactMapper;
import com.app.contactmanagementsystem.repository.ContactsRepository;
import com.app.contactmanagementsystem.repository.model.ContactEntity;
import com.app.contactmanagementsystem.service.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactsRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContactService contactService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
    }

    private ContactEntity buildContactEntity(Long id, LocalDateTime createdAt) {
        ContactEntity entity = new ContactEntity();
        entity.setId(id);
        entity.setUserId(userId);
        entity.setName("Contact" + id);
        entity.setSurname("Surname" + id);
        entity.setCreatedAt(createdAt.atZone(ZoneId.of("Europe/Rome")).toInstant());
        return entity;
    }

    private ContactResponseDTO buildContactResponseDto(Long id) {
        ContactResponseDTO dto = new ContactResponseDTO();
        dto.setId(id);
        dto.setName("Contact" + id);
        dto.setSurname("Surname" + id);
        return dto;
    }

    // ---------- getUserContacts ----------

    @Test
    void getUserContacts_shouldReturnContactsSortedByCreatedAtDescending() {
        ContactEntity older = buildContactEntity(1L, LocalDateTime.now().minusDays(2));
        ContactEntity newer = buildContactEntity(2L, LocalDateTime.now());

        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of(older, newer));
        when(contactMapper.toDto(older)).thenReturn(buildContactResponseDto(1L));
        when(contactMapper.toDto(newer)).thenReturn(buildContactResponseDto(2L));

        List<ContactResponseDTO> result = contactService.getUserContacts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L); // newer first
        assertThat(result.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void getUserContacts_shouldReturnEmptyList_whenNoContactsExist() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of());

        List<ContactResponseDTO> result = contactService.getUserContacts();

        assertThat(result).isEmpty();
    }

    // ---------- createContact ----------

    @Test
    void createContact_shouldSaveEntityWithUserIdAndReturnGeneratedId() {
        ContactCreationDTO creationDTO = new ContactCreationDTO();
        creationDTO.setName("John");
        creationDTO.setSurname("Smith");
        creationDTO.setPhone("123456789");

        ContactEntity mappedEntity = new ContactEntity();
        mappedEntity.setName("John");
        mappedEntity.setSurname("Smith");
        mappedEntity.setPhone("123456789");

        ContactEntity savedEntity = buildContactEntity(10L, LocalDateTime.now());

        when(userService.getCurrentUser()).thenReturn(user);
        when(contactMapper.toEntity(creationDTO)).thenReturn(mappedEntity);
        when(contactRepository.save(mappedEntity)).thenReturn(savedEntity);

        Long result = contactService.createContact(creationDTO);

        assertThat(result).isEqualTo(10L);
        assertThat(mappedEntity.getUserId()).isEqualTo(userId);

        ArgumentCaptor<ContactEntity> captor = ArgumentCaptor.forClass(ContactEntity.class);
        verify(contactRepository).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(userId);
    }

    // ---------- getContactById ----------

    @Test
    void getContactById_shouldReturnMappedDto_whenContactExists() {
        ContactEntity entity = buildContactEntity(5L, LocalDateTime.now());
        ContactResponseDTO expectedDto = buildContactResponseDto(5L);

        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of(entity));
        when(contactMapper.toDto(entity)).thenReturn(expectedDto);

        ContactResponseDTO result = contactService.getContactById(5L);

        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getContactById_shouldThrowContactNotFoundException_whenContactDoesNotExist() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of());

        assertThatThrownBy(() -> contactService.getContactById(99L))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact not found");
    }

    @Test
    void getContactById_shouldThrowContactNotFoundException_whenContactBelongsToDifferentId() {
        ContactEntity entity = buildContactEntity(5L, LocalDateTime.now());

        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of(entity));

        assertThatThrownBy(() -> contactService.getContactById(999L))
                .isInstanceOf(ContactNotFoundException.class);
    }

    // ---------- updateContact ----------

    @Test
    void updateContact_shouldUpdateFieldsAndReturnMappedDto_whenContactExists() {
        ContactEntity entity = buildContactEntity(7L, LocalDateTime.now());

        ContactCreationDTO updateDTO = new ContactCreationDTO();
        updateDTO.setName("UpdatedName");
        updateDTO.setSurname("UpdatedSurname");
        updateDTO.setPhone("999999999");

        ContactResponseDTO expectedDto = buildContactResponseDto(7L);

        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of(entity));
        when(contactMapper.toDto(entity)).thenReturn(expectedDto);

        ContactResponseDTO result = contactService.updateContact(7L, updateDTO);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(entity.getName()).isEqualTo("UpdatedName");
        assertThat(entity.getSurname()).isEqualTo("UpdatedSurname");
        assertThat(entity.getPhone()).isEqualTo("999999999");

        verify(contactRepository).save(entity);
    }

    @Test
    void updateContact_shouldThrowContactNotFoundException_whenContactDoesNotExist() {
        ContactCreationDTO updateDTO = new ContactCreationDTO();
        updateDTO.setName("UpdatedName");

        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of());

        assertThatThrownBy(() -> contactService.updateContact(123L, updateDTO))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact not found");

        verify(contactRepository, never()).save(any());
    }

    // ---------- deleteContact ----------

    @Test
    void deleteContact_shouldCallRepositoryDelete_whenContactExists() {
        ContactEntity entity = buildContactEntity(3L, LocalDateTime.now());
        ContactResponseDTO dto = buildContactResponseDto(3L);

        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of(entity));
        when(contactMapper.toDto(entity)).thenReturn(dto);

        contactService.deleteContact(3L);

        verify(contactRepository, times(1)).deleteById(3L);
    }

    @Test
    void deleteContact_shouldThrowContactNotFoundException_andNotCallDelete_whenContactDoesNotExist() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(contactRepository.findByUserId(userId)).thenReturn(List.of());

        assertThatThrownBy(() -> contactService.deleteContact(404L))
                .isInstanceOf(ContactNotFoundException.class);

        verify(contactRepository, never()).deleteById(eq(404L));
    }
}
