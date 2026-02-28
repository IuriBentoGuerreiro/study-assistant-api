package com.ibgs.studyAssistant.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendPasswordResetEmail(String to, String token) {

        String link = resetPasswordUrl + "?token=" + token;

        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "from", "onboarding@resend.dev",
                "to", List.of(to),
                "subject", "Recuperação de senha",
                "html", """
                        <p>Olá,</p>
                        <p>Recebemos uma solicitação para redefinir sua senha.</p>
                        <p>Clique no link abaixo para criar uma nova:</p>
                        <a href="%s">Redefinir senha</a>
                        <p>Este link expira em 30 minutos.</p>
                        """.formatted(link)
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}