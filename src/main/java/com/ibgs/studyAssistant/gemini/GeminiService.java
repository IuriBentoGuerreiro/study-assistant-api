package com.ibgs.studyAssistant.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibgs.studyAssistant.question.dto.QuestionGenerateDTO;
import com.ibgs.studyAssistant.question.enuns.QuestionType;
import com.ibgs.studyAssistant.studySession.dto.PromptRequest;
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
            Você é um especialista em elaboração de questões para concursos públicos, com profundo conhecimento do estilo, do nível de dificuldade e dos padrões de cobrança da banca {banca}.
            
            Contexto do concurso (considere apenas se as informações forem fornecidas):
            - Cargo: {cargo}
            - Cidade: {cidade}
            - Estado: {estado}
            - Nível do concurso: {nivel} (médio ou superior)
            
            Caso alguma dessas informações não seja fornecida, desconsidere-a completamente.
            
            Gere exatamente {quantidade} questões de múltipla escolha com base no conteúdo fornecido pelo usuário.
            
            Diretrizes obrigatórias:
            - As questões devem ser de nível alto, compatíveis com provas reais da banca {banca}.
            - Adeque o nível de profundidade ao nível do concurso ({nivel}), quando informado.
            - Sempre que possível, utilize questões que já tenham sido cobradas em concursos públicos anteriores, compatíveis com o cargo informado.
            - Caso não seja possível reproduzir uma questão real, crie uma questão inédita, porém MUITO semelhante ao estilo da banca {banca}, mantendo:
              - linguagem técnica
              - nível de aprofundamento compatível com o cargo
              - pegadinhas conceituais comuns da banca
              - cobrança literal de conceitos, normas ou definições quando aplicável
            - Evite questões genéricas, introdutórias ou de nível básico.
            - Não explique as respostas.
            - Não faça comentários, introduções ou conclusões.
            
            Estrutura de cada questão:
            - "statement": enunciado claro, objetivo e compatível com provas oficiais.
            - "options": exatamente 4 alternativas.
            - "correctAnswerIndex": número inteiro de 0 a 3, indicando a alternativa correta.
            
            Regras de formatação:
            - Retorne APENAS um JSON válido.
            - NÃO utilize Markdown.
            - NÃO inclua qualquer texto fora do JSON.
            - NÃO utilize comentários.
            - Sempre escreva em português do Brasil.
            - As alternativas devem seguir o formato "(A)", "(B)", "(C)", "(D)".
            
            Formato obrigatório de saída:
            [
              {
                "type": "MULTIPLE_CHOICE",
                "statement": "Pergunta aqui?",
                "options": ["(A) Opção 1", "(B) Opção 2", "(C) Opção 3", "(D) Opção 4"],
                "correctAnswerIndex": 1
              }
            ]
            """;

    private static final String PROMPT_GENERATE_TRUE_FALSE = """
            Você é um especialista em elaboração de questões para concursos públicos, com profundo conhecimento do estilo, do nível de dificuldade e dos padrões de cobrança da banca {banca}, especialmente no modelo CERTO ou ERRADO utilizado pelo CEBRASPE.
            
            Contexto do concurso (considere apenas se as informações forem fornecidas):
            - Cargo: {cargo}
            - Cidade: {cidade}
            - Estado: {estado}
            - Nível do concurso: {nivel} (médio ou superior)
            - Orgão do concurso: {orgao} (para qual orgão o concurso será realizado)
            
            Caso alguma dessas informações não seja fornecida, desconsidere-a completamente.
            
            Gere exatamente {quantidade} questões do tipo CERTO ou ERRADO com base no conteúdo fornecido pelo usuário.
            
            Diretrizes obrigatórias:
            - As questões devem seguir rigorosamente o modelo CEBRASPE (uma assertiva para julgamento).
            - O nível deve ser alto, compatível com provas reais da banca {banca}.
            - Adeque o grau de tecnicidade ao cargo e ao nível do concurso ({nivel}), quando informados.
            - Sempre que possível, utilize assertivas que já tenham sido cobradas em concursos públicos anteriores.
            - Caso não seja possível reproduzir uma assertiva real, crie uma inédita, porém MUITO semelhante ao estilo da banca {banca}, mantendo:
              - linguagem técnica e formal
              - alto nível de precisão conceitual
              - pegadinhas conceituais comuns da banca
              - afirmações absolutas, restritivas ou sutis quando aplicável
            - Evite assertivas óbvias, introdutórias ou excessivamente genéricas.
            - Não explique as respostas.
            - Não faça comentários, introduções ou conclusões.
            
            Estrutura de cada questão:
            - "statement": uma assertiva clara, objetiva e passível de julgamento como CERTO ou ERRADO.
            - "correctAnswerIndex":
              - 0 para CERTO
              - 1 para ERRADO
            
            Regras de formatação:
            - Retorne APENAS um JSON válido.
            - NÃO utilize Markdown.
            - NÃO inclua qualquer texto fora do JSON.
            - NÃO utilize comentários.
            - Sempre escreva em português do Brasil.
            
            Formato obrigatório de saída:
            [
              {
                "type": "TRUE_FALSE",
                "statement": "Assertiva para julgamento.",
                "correctAnswerIndex": 0
              }
            ]
            """;

    private static final String PROMPT_SUMMARY = """
            Você é um assistente de estudos especializado em preparação para concursos públicos.
            
            Sua tarefa é **resumir o conteúdo abaixo** de forma estratégica para estudo e memorização, seguindo rigorosamente as diretrizes:
            
             **Objetivo do resumo**
            - Facilitar a memorização
            - Destacar o que mais cai em provas
            - Ajudar na revisão rápida antes da prova
            
             **Estrutura obrigatória**
            1. **Tópicos principais do conteúdo**
               - Apenas assuntos realmente relevantes para concursos
               - Nada superficial ou redundante
            
            2. **Frases curtas, diretas e objetivas**
               - Linguagem clara e didática
               - Estilo “frase de prova”
               - Evite textos longos ou explicações excessivas
            
            3. **Foco em nível avançado**
               - Extraia definições, classificações, conceitos-chave, exceções e observações importantes
               - Dê prioridade ao que costuma ser cobrado em provas objetivas e discursivas
            
             **Formato e linguagem**
            - Utilize **Markdown**
            - Destaque conceitos importantes em **negrito**
            - Use listas, subtópicos e organização visual
            - Utilize ícones como foguete, lápis, caderno, régua e afins para reforçar os pontos mais importantes (use mas não abuse)
            - Linguagem: **Português do Brasil**
            
             **Regras importantes**
            - Não invente informações
            - Não inclua opiniões
            - Não faça introduções ou conclusões genéricas
            - Foque exclusivamente no conteúdo fornecido
            
            **Use bastante a linguagem markdown**
            - Para deixar títulos e subtítulos de tamanhos diferentes e destacados cada um com uma cor padrão.
            - para deixar a visualização mais dinâmica e não chata.
            - para interagir melhor com o usuário.
            
            **Conteúdo para resumo:**
            """;


    public GeminiService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public List<QuestionGenerateDTO> generateQuestions(PromptRequest request) {

        String promptFinal = buildPrompt(request);

        String raw = callGemini(promptFinal);
        String json = extractJsonArray(raw);

        try {
            List<QuestionGenerateDTO> questions =
                    objectMapper.readValue(
                            json,
                            new TypeReference<List<QuestionGenerateDTO>>() {}
                    );

            validateQuestions(questions, request.type());
            return questions;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar questões", e);
        }
    }

    public String generateSummary(String content) {
        String prompt = PROMPT_SUMMARY + "\n" + truncateText(content);
        return callGemini(prompt);
    }

    private String buildPrompt(PromptRequest request) {
        return getPromptByType(request.type())
                + field("Banca", request.banca())
                + field("Órgão", request.orgao())
                + field("Cargo", request.cargo())
                + field("Cidade", request.cidade())
                + field("Estado", request.estado())
                + field("Nível", request.nivel())
                + "\nQuantidade: " + request.quantidade()
                + "\n\nConteúdo base:\n" + request.prompt();
    }

    private String field(String label, String value) {
        return value == null || value.isBlank()
                ? ""
                : "\n" + label + ": " + value;
    }

    private String truncateText(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return "";
        }

        if (prompt.length() > 3000) {
            return prompt.substring(0, 3000);
        }

        return prompt;
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
                } catch (InterruptedException ignored) {
                }
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

    private String extractJsonArray(String text) {

        int start = text.indexOf("[");
        int end = text.lastIndexOf("]");

        if (start == -1 || end == -1 || end <= start) {
            throw new RuntimeException("Resposta do Gemini não contém JSON válido");
        }

        return text.substring(start, end + 1);
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

    private String getPromptByType(QuestionType type) {
        return switch (type) {
            case TRUE_FALSE -> PROMPT_GENERATE_TRUE_FALSE;
            case MULTIPLE_CHOICE -> PROMPT_GENERATE;
        };
    }

    private void validateQuestions(List<QuestionGenerateDTO> questions, QuestionType type) {

        for (QuestionGenerateDTO q : questions) {

            if (q.statement() == null || q.statement().isBlank()) {
                throw new RuntimeException("Questão sem enunciado");
            }

            if (q.type() == null || q.type() != type) {
                throw new RuntimeException("Tipo da questão inconsistente: " + q);
            }

            switch (type) {
                case MULTIPLE_CHOICE -> {
                    if (q.options() == null || q.options().size() != 4) {
                        throw new RuntimeException("Questão de múltipla escolha inválida: " + q);
                    }
                    if (q.correctAnswerIndex() < 0 || q.correctAnswerIndex() > 3) {
                        throw new RuntimeException("Índice de resposta inválido: " + q);
                    }
                }

                case TRUE_FALSE -> {
                    if (q.options() != null && !q.options().isEmpty()) {
                        throw new RuntimeException("Questão TRUE_FALSE não deve ter opções: " + q);
                    }
                    if (q.correctAnswerIndex() < 0 || q.correctAnswerIndex() > 1) {
                        throw new RuntimeException("Resposta TRUE_FALSE inválida: " + q);
                    }
                }
            }
        }
    }

}