package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }
}
