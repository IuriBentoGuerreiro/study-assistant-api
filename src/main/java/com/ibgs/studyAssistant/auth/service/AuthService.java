package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.auth.model.Role;
import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(
            RoleService roleService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserService userService,
            JwtUtil jwtUtil,
            CustomUserDetailsService userDetailsService
    ) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public ResponseEntity<Map<String, String>> login(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        String token = jwtUtil.generateToken(userDetails.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    public User register(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        Role userRole = roleService.findByname(RoleName.ROLE_USER);

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User newUser = new User(user.getUsername(), encryptedPassword, roles);

        return userService.save(newUser);
    }
}
