package com.ibgs.studyAssistant.auth.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Async
    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            String link = resetPasswordUrl + "?token=" + token;
            SimpleMailMessage message = createMessage(to, link);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail");
        }
    }

    private SimpleMailMessage createMessage(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperação de senha");
        message.setText("""
            Olá,
            
            Recebemos uma solicitação para redefinir sua senha.
            Clique no link abaixo para criar uma nova:
            %s
            
            Este link expira em 30 minutos.
            """.formatted(link));
        return message;
    }
}