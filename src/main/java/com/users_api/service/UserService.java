package com.users_api.service;

import com.users_api.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    User createUser(User user);
    User getUser(Long id);
    List<User> getAll();
    User updateUser(User user);
    User patchUser(User user);
    void delete(Long id);

    List<User> findUsersByDateOfBirth(LocalDate from, LocalDate to);
}
