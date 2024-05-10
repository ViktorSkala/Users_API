package com.users_api.repository;

import com.users_api.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetUser() {
        User user = User.builder()
                .id(1L)
                .email("nick@mail.com")
                .firstName("Nick")
                .lastName("Green")
                .dateOfBirth(LocalDate.parse("1990-05-05"))
                .address("Address 1")
                .phoneNumber("123123123")
                .build();
        User result = userRepository.findById(1L).get();
        Assertions.assertEquals(user, result);
    }

    @Test
    @Transactional
    public void testCreateUser() {
        User user = User.builder()
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.parse("1990-01-01"))
                .build();
        User result = userRepository.save(user);
        Assertions.assertTrue(Objects.nonNull(result.getId()));
        Assertions.assertEquals(result.getEmail(), "test@mail.com");
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        User user = User.builder()
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.parse("1990-01-01"))
                .build();
        User savedUser = userRepository.save(user);
        savedUser.setEmail("updated@mail.com");
        User updatedUser = userRepository.save(savedUser);
        Assertions.assertEquals(updatedUser.getEmail(), "updated@mail.com");
    }

    @Test
    public void testGetAllUsers() {
        List<User> allUsers = userRepository.findAll();
        Assertions.assertTrue(allUsers.size() == 3);
    }

    @Test
    public void testFindByDateRange() {
        LocalDate from = LocalDate.of(1989, Month.JANUARY, 1);
        LocalDate to = LocalDate.of(2000, Month.JANUARY, 1);
        List<User> allUsers = userRepository.findByDateOfBirthAfterAndDateOfBirthBefore(from, to);
        Assertions.assertTrue(allUsers.size() == 2);
    }

    @Test
    public void testFindByParameters() {
        List<User> allUsers = userRepository.findByParameters(null, "Nick", null, null, LocalDate.of(1989, 1, 1), LocalDate.of(2000, 1, 1), null, null);
        Assertions.assertTrue(allUsers.size() == 1);
        Assertions.assertTrue(allUsers.get(0).getFirstName().equals("Nick"));
    }
}
