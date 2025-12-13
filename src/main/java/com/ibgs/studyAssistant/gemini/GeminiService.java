package com.ibgs.studyAssistant.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String PROMPT_STANDARD = """
           Você é um assistente de estudos especializado em concursos públicos.

Com base no conteúdo enviado:
- Gere 10 questões de múltipla escolha
- Cada questão deve conter:
  - Enunciado
  - Alternativas (A, B, C, D)
  - Resposta correta
- Utilize formatação Markdown
- Sempre em português do Brasil
- Não precisa retornar mais nada como mensagens complementares, apenas o conteúdo solicitado
""";


    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String generateQuestions(String promptUser) {

        String promptFinal = PROMPT_STANDARD + "\n\nConteúdo base:\n" + promptUser;

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", promptFinal)
                                )
                        )
                )
        );

        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .queryParam("key", apiKey)
                        .build()
                )
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractText(response);
    }

    private String extractText(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            return root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta do Gemini", e);
        }
    }
}
