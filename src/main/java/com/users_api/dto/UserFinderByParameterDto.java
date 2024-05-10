package com.users_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFinderByParameterDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirthFrom;
    private LocalDate dateOfBirthTo;
    private String address;
    private String phoneNumber;
}
