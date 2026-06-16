package com.app.contactmanagementsystem.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ContactResponseDTO {
    
    private Long id;
    
    private String name;

    private String surname;

    private String phone;
}
