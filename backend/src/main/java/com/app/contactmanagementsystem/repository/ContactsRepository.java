package com.app.contactmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.contactmanagementsystem.model.ContactEntity;

@Repository
public interface ContactsRepository extends JpaRepository<ContactEntity, Long> {

}