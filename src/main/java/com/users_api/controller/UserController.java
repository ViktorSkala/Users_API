package com.users_api.controller;

import com.users_api.dto.UserDto;
import com.users_api.mapper.UserMapper;
import com.users_api.model.User;
import com.users_api.request.UserRequestDto;
import com.users_api.response.UserResponseDto;
import com.users_api.response.UsersResponseDto;
import com.users_api.service.UserService;
import com.users_api.validator.Create;
import com.users_api.validator.DateRange;
import com.users_api.validator.Patch;
import com.users_api.validator.Update;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(UsersResponseDto.builder()
                .data(userService.getAll().stream()
                        .map(UserMapper::toDto)
                        .collect(Collectors.toList()))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(UserResponseDto.builder()
                .data(UserMapper.toDto(userService.getUser(id)))
                .build());
    }

    @PostMapping
    @Validated(Create.class)
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserDto createdUserDto = UserMapper.toDto(userService.createUser(UserMapper.fromDto(requestDto.getData())));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        URI location = uriBuilder.path("/users/{id}").buildAndExpand(createdUserDto.getId()).toUri();
        return ResponseEntity.created(location)
                .body(UserResponseDto.builder()
                        .data(createdUserDto)
                        .build());
    }

    @PutMapping("/{id}")
    @Validated(Update.class)
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequestDto requestDto,
                                        @PathVariable("id") long id) {
        User user = UserMapper.fromDto(requestDto.getData());
        user.setId(id);
        UserDto updatedUserDto = UserMapper.toDto(userService.updateUser(user));
        return ResponseEntity.ok()
                .body(UserResponseDto.builder()
                        .data(updatedUserDto)
                        .build());
    }

    @PatchMapping("/{id}")
    @Validated(Patch.class)
    public ResponseEntity<?> patchUser(@Valid @RequestBody UserRequestDto requestDto,
                                       @PathVariable("id") long id) {
        User user = UserMapper.fromDto(requestDto.getData());
        user.setId(id);
        UserDto patchedUserDto = UserMapper.toDto(userService.patchUser(user));
        return ResponseEntity.ok()
                .body(UserResponseDto.builder()
                        .data(patchedUserDto)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<?> filterUsersByDateOfBirth(@DateRange @RequestParam("daterange") String dateRange) {
        String[] dates = dateRange.split(",");
        LocalDate from = LocalDate.parse(dates[0], DateTimeFormatter.ISO_DATE);
        LocalDate to = LocalDate.parse(dates[1], DateTimeFormatter.ISO_DATE);
        List<UserDto> result = userService.findUsersByDateOfBirth(from, to).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(UsersResponseDto.builder().data(result).build());
    }
}
