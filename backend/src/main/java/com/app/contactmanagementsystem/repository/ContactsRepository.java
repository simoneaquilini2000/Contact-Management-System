package com.app.contactmanagementsystem.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.contactmanagementsystem.repository.model.ContactEntity;

@Repository
public interface ContactsRepository extends JpaRepository<ContactEntity, Long> {

    Collection<ContactEntity> findByUserId(UUID id);
    
}