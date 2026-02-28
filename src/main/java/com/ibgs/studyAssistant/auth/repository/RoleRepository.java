package com.ibgs.studyAssistant.auth.repository;

import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleName roleName);
}