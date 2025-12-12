package com.ibgs.studyAssistant.auth.repository;

import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}