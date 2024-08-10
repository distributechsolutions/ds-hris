package com.victorylimited.hris.repositories.admin;

import com.victorylimited.hris.entities.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("""
           SELECT u FROM User u
           WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(u.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(u.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(u.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(u.employee.suffix) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(u.employee.gender) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(u.emailAddress) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(u.role) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<User> findByStringParameter(@Param("param") String parameter);

    @Query("""
           SELECT u FROM User u
           WHERE u.accountLocked = :param
           OR u.accountActive = :param
           OR u.passwordChanged = :param
           """)
    List<User> findByBooleanParameter(@Param("param") boolean param);
}
