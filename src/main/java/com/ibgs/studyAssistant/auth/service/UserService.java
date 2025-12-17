package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public User findById(Integer userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Usuário Não Encontrado")
        );
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Usuário Não Encontrado")
        );
    }

}
