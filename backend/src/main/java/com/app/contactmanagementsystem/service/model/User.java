package com.app.contactmanagementsystem.service.model;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class User {

    private UUID id;
    private String name;
    private String email;
}
