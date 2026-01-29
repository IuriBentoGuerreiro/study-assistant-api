package com.ibgs.studyAssistant.auth.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Override
    public void sendPasswordResetEmail(String to, String token) {

        String link = resetPasswordUrl + "?token=" + token;

        String subject = "Recuperação de senha";

        String body = """
            Olá,

            Recebemos uma solicitação para redefinir sua senha.

            Para criar uma nova senha, clique no link abaixo:
            %s

            Este link expira em 30 minutos.

            Se você não solicitou a recuperação, ignore este email.

            Atenciosamente,
            Equipe de Suporte
            """.formatted(link);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
