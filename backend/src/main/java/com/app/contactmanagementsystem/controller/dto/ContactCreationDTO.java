package com.app.contactmanagementsystem.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class ContactCreationDTO {

    private String name;

    private String surname;

    private String phone;
}
