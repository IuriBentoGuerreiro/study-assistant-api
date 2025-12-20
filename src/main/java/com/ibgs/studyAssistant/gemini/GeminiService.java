package com.ibgs.studyAssistant.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibgs.studyAssistant.dto.QuestionGenerateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String PROMPT_GENERATE = """
        Você é um assistente de estudos especializado em concursos públicos.
        
        Gere exatamente 10 questões de múltipla escolha com base no conteúdo fornecido.
        
        Regras:
        - Cada questão deve conter:
          - statement (a pergunta)
          - options (4 alternativas)
          - correctAnswerIndex (índice da resposta correta como número inteiro de 0 a 3)
        - A alternativa correta deve estar em correctAnswerIndex (0 = primeira alternativa, 1 = segunda, 2 = terceira, 3 = quarta)
        - Retorne APENAS um JSON válido
        - NÃO use Markdown
        - NÃO inclua texto fora do JSON
        - Sempre em português do Brasil
        
        Formato obrigatório:
        [
          {
            "statement": "Pergunta aqui?",
            "options": ["(A) Opção 1", "(B) Opção 2", "(C) Opção 3", "(D) Opção 4"],
            "correctAnswerIndex": 1
          }
        ]
        
        Exemplo:
        [
          {
            "statement": "Qual é a capital do Brasil?",
            "options": ["(A) São Paulo", "(B) Brasília", "(C) Rio de Janeiro", "(D) Salvador"],
            "correctAnswerIndex": 1
          }
        ]
        """;

    public GeminiService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public List<QuestionGenerateDTO> generateQuestions(String promptUser) {

        String promptFinal = PROMPT_GENERATE + "\n\nConteúdo base:\n" + promptUser;
        String json = callGemini(promptFinal);

        try {
            List<QuestionGenerateDTO> questions =
                    objectMapper.readValue(json, new TypeReference<>() {});

            questions.forEach(q -> {
                if (q.options() == null || q.options().size() != 4) {
                    throw new RuntimeException("Questão inválida: " + q);
                }
            });

            return questions;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar questões", e);
        }
    }

    private String callGemini(String prompt) {
        int tentativas = 0;

        while (tentativas < 3) {
            try {
                return doCall(prompt);
            } catch (HttpClientErrorException.TooManyRequests e) {
                tentativas++;
                try {
                    Thread.sleep(2000L * tentativas);
                } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("Limite da API Gemini atingido.");
    }

    private String doCall(String prompt) {

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        String response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .queryParam("key", apiKey)
                        .build()
                )
                .body(body)
                .retrieve()
                .body(String.class);

        return extractText(response);
    }

    private String extractText(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);

            return root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText()
                    .trim();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta do Gemini", e);
        }
    }
}