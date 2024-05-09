package com.users_api.model;

import com.users_api.validator.Create;
import com.users_api.validator.Patch;
import com.users_api.validator.Update;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private Validator validator;

    @Test
    public void testCreateValidUser() {
        User user = User.builder()
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.parse("1990-01-01"))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertEquals(0, violations.size());
    }

    @Test
    public void testCreateInvalidUser() {
        User user = User.builder()
                .email("qwerty")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);

        Assertions.assertEquals(4, violations.size());
    }

    @Test
    public void testUpdateInvalidUser() {
        User user = User.builder()
                .email("qwerty")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);

        Assertions.assertEquals(4, violations.size());
    }

    @Test
    public void testPatchInvalidUser() {
        User user = User.builder()
                .email("qwerty")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, Patch.class);

        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void testPatchValidUser() {
        User user = User.builder()
                .email("test@mail.ua")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, Patch.class);

        Assertions.assertEquals(0, violations.size());
    }

}
