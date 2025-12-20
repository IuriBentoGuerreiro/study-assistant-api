package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.auth.model.Role;
import com.ibgs.studyAssistant.auth.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByname(RoleName roleName){
        return roleRepository.findByRoleName(roleName).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
    }
}
