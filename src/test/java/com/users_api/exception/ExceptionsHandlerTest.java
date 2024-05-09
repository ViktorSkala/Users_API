package com.users_api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.users_api.controller.UserController;
import com.users_api.dto.UserDto;
import com.users_api.request.UserRequestDto;
import com.users_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest({GlobalExceptionHandler.class, UserController.class})
public class ExceptionsHandlerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void testConstraintViolationException_ReturnJsonResponse() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        UserDto invalidUser = UserDto.builder()
                .email("test")
                .firstName("Name")
                .lastName("LastName")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserRequestDto.builder().data(invalidUser).build())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].detail").value("must be a well-formed email address"));
    }

    @Test
    public void testNoSuchElementException_ReturnJsonResponse() throws Exception {
        Mockito.when(userService.getUser(5L)).thenThrow(new NoSuchElementException("User with id=5 not found"));
        mockMvc.perform(get("/users/5"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].detail").value("User with id=5 not found"));
    }
}
