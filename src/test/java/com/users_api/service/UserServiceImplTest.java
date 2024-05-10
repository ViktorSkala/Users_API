package com.users_api.service;

import com.users_api.dto.UserFinderByParameterDto;
import com.users_api.model.User;
import com.users_api.repository.UserRepository;
import com.users_api.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.parse("1990-01-01"))
                .build();
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.createUser(new User());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void testGetUser() {
        User user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.parse("1990-01-01"))
                .build();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.getUser(1L);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void testGetUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        String expected = "User with id=1 not found";
        Throwable exception = assertThrows(NoSuchElementException.class, () -> userService.getUser(1L));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testGetAll() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.parse("1990-01-01"))
                .build();
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.updateUser(user);
        assertEquals(user, result);
    }

    @Test
    public void testPatchUser() {
        User user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .build();
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.patchUser(user);
        assertEquals(user, result);
    }

    @Test
    public void testDelete() {
        User user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .build();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.delete(1L));
    }

    @Test
    public void testDeleteUserNotFound() {
        String expected = "User with id=1 not found";
        Mockito.when(userRepository.findById(1L)).thenThrow(new NoSuchElementException("User with id=1 not found"));
        Throwable exception = assertThrows(NoSuchElementException.class, () -> userService.delete(1L));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testFindUsersByDateOfBirth() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 12, 31);
        Mockito.when(userRepository.findByDateOfBirthAfterAndDateOfBirthBefore(fromDate, toDate)).thenReturn(userList);
        List<User> result = userService.findUsersByDateOfBirth(fromDate, toDate);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindUsersByParameters() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        Mockito.when(userRepository.findByParameters(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(userList);
        UserFinderByParameterDto userFinderByParameterDto = UserFinderByParameterDto.builder().firstName("Nick").build();
        List<User> result = userService.findUsersByParameters(userFinderByParameterDto);
        assertEquals(1, result.size());
    }

}

