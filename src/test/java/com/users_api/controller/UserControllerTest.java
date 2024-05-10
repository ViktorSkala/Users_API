package com.users_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.users_api.dto.UserDto;
import com.users_api.dto.UserFinderByParameterDto;
import com.users_api.model.User;
import com.users_api.request.UserRequestDto;
import com.users_api.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private static ObjectMapper objectMapper;
    private static User user;
    private static UserDto userDto;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
        userDto = UserDto.builder()
                .email("test@mail.com")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    public void testGetAllUsers_ReturnList() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        Mockito.when(userService.getAll()).thenReturn(userList);
        mockMvc.perform(get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(userList.size()));
    }

    @Test
    public void testGetUser_ReturnInstance() throws Exception {
        Mockito.when(userService.getUser(1L)).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(user.getId()));
    }

    @Test
    public void testCreateUser_ReturnCreated() throws Exception {
        Mockito.when(userService.createUser(ArgumentMatchers.any(User.class))).thenReturn(user);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserRequestDto.builder().data(userDto).build())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/users/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email", CoreMatchers.is(userDto.getEmail())));
    }

    @Test
    public void testUpdateUser_ReturnOk() throws Exception {
        Mockito.when(userService.updateUser(any(User.class))).thenReturn(user);
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserRequestDto.builder().data(userDto).build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(userDto.getEmail()));
    }

    @Test
    public void testPatchUser_ReturnOk() throws Exception {
        Mockito.when(userService.patchUser(any(User.class))).thenReturn(user);
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"data\":{\"dateOfBirth\":\"1990-01-01\"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dateOfBirth").value(userDto.getDateOfBirth().format(DateTimeFormatter.ISO_DATE)));
    }

    @Test
    public void testDeleteUser_ReturnOk() throws Exception {
        Mockito.doNothing().when(userService).delete(anyLong());
        mockMvc.perform(delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFilterUsersByParameterBirthDay_ReturnList() throws Exception {
        LocalDate from = LocalDate.of(2010, 1, 1);
        LocalDate to = LocalDate.of(2020, 1, 1);
        Mockito.when(userService.findUsersByParameters(any(UserFinderByParameterDto.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/users/")
                        .param("daterange", from.format(DateTimeFormatter.ISO_DATE) + "," + to.format(DateTimeFormatter.ISO_DATE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFilterUsersByParameterName_ReturnList() throws Exception {
        Mockito.when(userService.findUsersByParameters(any(UserFinderByParameterDto.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/users/")
                        .param("name", "Nick"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateUser_ValidationFailure() throws Exception {
        UserDto invalidUser = UserDto.builder()
                .email("test")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserRequestDto.builder().data(invalidUser).build())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(4));
    }

    @Test
    public void testUpdateUser_ValidationFailure() throws Exception {
        UserDto invalidUser = UserDto.builder()
                .email("test@mail.com")
                .build();
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserRequestDto.builder().data(invalidUser).build())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(3));
    }

    @Test
    public void testPatchUser_ValidationFailure() throws Exception {
        UserDto invalidUser = UserDto.builder()
                .email("test")
                .build();
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserRequestDto.builder().data(invalidUser).build())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(1));
    }
}