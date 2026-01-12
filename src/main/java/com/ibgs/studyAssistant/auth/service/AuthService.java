package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.dto.AuthMeResponse;
import com.ibgs.studyAssistant.auth.dto.LoginRequest;
import com.ibgs.studyAssistant.auth.dto.LoginResponse;
import com.ibgs.studyAssistant.auth.dto.RefreshTokenRequest;
import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.auth.model.Role;
import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.utils.JwtUtil;
import com.ibgs.studyAssistant.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public ResponseEntity<LoginResponse> login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        User user = (User) authentication.getPrincipal();

        assert user != null;

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken)
        );    }

    public User register(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        Role userRole = roleService.findByname(RoleName.ROLE_USER);

        Set<Role> roles = Set.of(userRole);

        User newUser = new User(
                user.getUsername(),
                encryptedPassword,
                roles
        );

        return userService.save(newUser);
    }

    public ResponseEntity<LoginResponse> refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.refreshToken();

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Refresh token inválido ou expirado");
        }

        String username = jwtUtil.extractUsername(refreshToken);

        User user = userService.findByUsername(username);

        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        return ResponseEntity.ok(
                new LoginResponse(newAccessToken, newRefreshToken)
        );
    }

    public AuthMeResponse getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidTokenException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            throw new InvalidTokenException("Principal inválido");
        }

        return new AuthMeResponse(
                user.getId(),
                user.getUsername(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}
