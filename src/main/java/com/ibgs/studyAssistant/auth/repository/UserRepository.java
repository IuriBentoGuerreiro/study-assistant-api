package com.ibgs.studyAssistant.auth.repository;

import com.ibgs.studyAssistant.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
        select distinct u
        from User u
        left join fetch u.roles
        where u.username = :username
    """)
    Optional<User> findByUsername(String username);
}
