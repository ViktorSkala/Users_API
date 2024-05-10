package com.users_api.repository;

import com.users_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.dateOfBirth >= ?1 and u.dateOfBirth <= ?2")
    List<User> findByDateOfBirthAfterAndDateOfBirthBefore(LocalDate from, LocalDate to);

    @Query("""
            select u from User u
            where (:userId is null or u.id = :userId) 
            and (:firstName is null or u.firstName = :firstName) 
            and (:lastName is null or u.lastName = :lastName ) 
            and (:email is null or u.email = :email) 
            and (:dateOfBirthFrom <= u.dateOfBirth) 
            and (:dateOfBirthTo >= u.dateOfBirth)
            and (:address is null or u.address = :address) 
            and (:phoneNumber is null or u.phoneNumber = :phoneNumber) 
            ORDER BY u.id ASC
            """)
    List<User> findByParameters(@Param("userId") Long userId,
                                        @Param("firstName") String firstName,
                                        @Param("lastName") String lastName,
                                        @Param("email") String email,
                                        @Param("dateOfBirthFrom") LocalDate dateOfBirthFrom,
                                        @Param("dateOfBirthTo") LocalDate dateOfBirthTo,
                                        @Param("address") String address,
                                        @Param("phoneNumber") String phoneNumber);
}
