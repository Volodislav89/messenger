package com.spring.messenger.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.messenger.model.Gender;
import com.spring.messenger.security.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long id;

    private String phoneNumber;

    private Gender gender;

    private Date dateOfBirth;

    private String address1;

    private String address2;

    private String street;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    @JsonIgnore
    private User user;

    private MultipartFile file;
}
