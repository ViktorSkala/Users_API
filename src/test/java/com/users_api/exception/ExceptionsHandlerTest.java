package com.users_api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.users_api.dto.UserDto;
import com.users_api.request.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(GlobalExceptionHandler.class)
@ActiveProfiles("test")
public class ExceptionsHandlerTest {

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].detail").value("email can not be empty"));
    }

    @Test
    public void testNoSuchElementException_ReturnJsonResponse() throws Exception {
        mockMvc.perform(delete("/users/5"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].detail").value("User with id=0 not found"));
    }
}
