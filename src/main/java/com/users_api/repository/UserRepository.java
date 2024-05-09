package com.users_api.repository;

import com.users_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.dateOfBirth >= ?1 and u.dateOfBirth <= ?2")
    List<User> findByDateOfBirthAfterAndDateOfBirthBefore(LocalDate from, LocalDate to);
}
