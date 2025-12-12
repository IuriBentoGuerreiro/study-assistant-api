package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.auth.model.Role;
import com.ibgs.studyAssistant.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByname(RoleName roleName){
        return roleRepository.findByRoleName(roleName).orElseThrow(
                ()-> new NotFoundException("Not found")
        );
    }
}
