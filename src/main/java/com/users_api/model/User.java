package com.users_api.model;

import com.users_api.validator.Adult;
import com.users_api.validator.Create;
import com.users_api.validator.Patch;
import com.users_api.validator.Update;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Custom_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Email(groups = {Create.class, Update.class, Patch.class})
    @NotBlank(message = "email can not be empty", groups = {Create.class, Update.class})
    private String email;
    @NotBlank(message = "name can not be empty", groups = {Create.class, Update.class})
    private String firstName;
    @NotBlank(message = "last name can not be empty", groups = {Create.class, Update.class})
    private String lastName;
    @Adult(groups = {Create.class, Update.class, Patch.class})
    @NotNull(message = "date of birth can not be empty", groups = {Create.class, Update.class})
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
}
