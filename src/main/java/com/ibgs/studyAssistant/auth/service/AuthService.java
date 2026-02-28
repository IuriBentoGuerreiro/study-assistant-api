package com.ibgs.studyAssistant.auth.service;

import com.ibgs.studyAssistant.auth.dto.LoginRequest;
import com.ibgs.studyAssistant.auth.dto.LoginResponse;
import com.ibgs.studyAssistant.auth.dto.RefreshTokenRequest;
import com.ibgs.studyAssistant.auth.dto.ResetPasswordRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    public void forgotPassword(String username) {

        User user = userService.findByUsername(username);

        passwordResetTokenRepository.deleteAllByUserId(user.getId());

        String rawToken = UUID.randomUUID().toString();

        String hashedToken = passwordEncoder.encode(rawToken);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(hashedToken);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getUsername(), rawToken);
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
        resetToken.setUsed(true);

        userService.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}
