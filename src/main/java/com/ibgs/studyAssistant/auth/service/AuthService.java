package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.dto.*;
import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.auth.model.PasswordResetToken;
import com.ibgs.studyAssistant.auth.model.Role;
import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.repository.PasswordResetTokenRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

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

    public void forgotPassword(String username) {

        User user = userService.findByUsername(username);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getUsername(), token);
    }

    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(request.token())
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new InvalidTokenException("Token expirado");
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(request.newPassword())
        );

        userService.save(user);

        passwordResetTokenRepository.delete(resetToken);
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
