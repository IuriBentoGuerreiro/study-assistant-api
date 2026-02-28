package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.dto.UserMeResponse;
import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.repository.UserRepository;
import com.ibgs.studyAssistant.exception.InvalidTokenException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public User findById(UUID userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Usuário Não Encontrado")
        );
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Usuário Não Encontrado")
        );
    }

    public UserMeResponse getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidTokenException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            throw new InvalidTokenException("Principal inválido");
        }

        return new UserMeResponse(
                user.getId(),
                user.getUsername(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }

    public User getReference(UUID id) {
        return userRepository.getReferenceById(id);
    }
}
