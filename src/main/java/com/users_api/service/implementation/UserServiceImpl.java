package com.users_api.service.implementation;

import com.users_api.dto.UserFinderByParameterDto;
import com.users_api.model.User;
import com.users_api.repository.UserRepository;
import com.users_api.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("User with id=" + id + " not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        getUser(user.getId());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User patchUser(User source) {
        User destination = getUser(source.getId());
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Arrays.stream(sourceFields).forEach(field -> {
            field.setAccessible(true);
            try {
                if (Objects.nonNull(field.get(source))) {
                    field.set(destination, field.get(source));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return userRepository.save(destination);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NoSuchElementException {
        getUser(id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findUsersByDateOfBirth(LocalDate from, LocalDate to) {
        return userRepository.findByDateOfBirthAfterAndDateOfBirthBefore(from, to);
    }

    @Override
    public List<User> findUsersByParameters(UserFinderByParameterDto userParameters) {
        return userRepository.findByParameters(userParameters.getUserId(),
                userParameters.getFirstName(),
                userParameters.getLastName(),
                userParameters.getEmail(),
                (userParameters.getDateOfBirthFrom() != null)? userParameters.getDateOfBirthFrom(): LocalDate.of(1900, 1, 1),
                (userParameters.getDateOfBirthTo() != null)? userParameters.getDateOfBirthTo(): LocalDate.of(2100, 1, 1),
                userParameters.getAddress(),
                userParameters.getPhoneNumber());
    }
}
