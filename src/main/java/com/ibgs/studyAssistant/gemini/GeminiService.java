package com.ibgs.studyAssistant.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibgs.studyAssistant.question.dto.QuestionGenerateDTO;
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

    private static final String PROMPT_BASE = """
            Você é um especialista em elaboração de questões.
            
            REGRA PRINCIPAL — USO DE QUESTÕES DE ALTA QUALIDADE:
            - Você deve retornar questões de alta qualidade, preferencialmente baseadas em fontes reais,
              como provas, vestibulares, exames, livros didáticos ou materiais reconhecidos.
            - Priorize questões que correspondam ao conteúdo, nível e contexto informados.
            - Caso não encontre questões diretamente compatíveis, adapte questões reais de forma fiel ao
              conteúdo solicitado, mantendo o nível de dificuldade e estilo.
            - Evite criar questões superficiais ou genéricas.
            
            TEXTOS DE APOIO:
            - Quando a questão exigir um texto de apoio, NÃO reproduza o texto completo.
            - Inclua apenas o trecho estritamente necessário para contextualizar e responder a questão.
            - O trecho deve ser inserido diretamente no campo "statement", antes do enunciado, usando markdown:
            
              *Texto:*
            
              [apenas o fragmento relevante, sem o texto completo]
            
              ---
              [enunciado da pergunta]
            
            - Quando a questão não exigir texto de apoio, o campo "statement" conterá apenas o enunciado normalmente.
            
            Diretrizes obrigatórias:
            - O nível das questões deve ser compatível com o nível informado ({nivel}).
            - Adeque o grau de tecnicidade ao contexto de estudo.
            - O campo "comment" deve obrigatoriamente conter a explicação técnica da resposta.
            - Use linguagem markdown no campo "comment" para organizar a explicação de forma didática.
            - NÃO inclua qualquer texto, saudação ou conclusão fora do JSON.
            
            Regras de formatação:
            - Retorne APENAS um JSON válido.
            - NÃO utilize Markdown fora dos campos "statement" e "comment".
            - NÃO inclua qualquer texto fora do JSON.
            - NÃO utilize comentários no JSON.
            - Sempre escreva em português do Brasil.
            
            Exemplo de questão com texto de apoio no statement:
            {
              "statement": "\\n*Texto:*\\n\\n[trecho]\\n\\n---\\nCom base no texto acima, assinale a alternativa correta.",
              "options": ["(A) Opção 1", "(B) Opção 2", "(C) Opção 3", "(D) Opção 4", "(E) Opção 5"],
              "correctAnswerIndex": 1,
              "comment": "Comentário detalhado justificando a resposta"
            }
            
            Exemplo de questão sem texto de apoio:
            {
              "statement": "Assertiva para julgamento.",
              "correctAnswerIndex": 0,
              "comment": "Comentário detalhado justificando a resposta"
            }
            """;

    private static final String PROMPT_MULTIPLE_CHOICE = PROMPT_BASE + """
            Gere exatamente {quantidade} questões de múltipla escolha com base no conteúdo fornecido pelo usuário.
            
            Estrutura de cada questão:
            - "statement": enunciado claro, objetivo e compatível com provas oficiais.
            - "options": exatamente 5 alternativas no formato "(A)", "(B)", "(C)", "(D)", (E).
            - "correctAnswerIndex": número inteiro de 0 a 4, indicando a alternativa correta.
            - "comment": explicação técnica detalhada da resposta correta.
            
            Formato obrigatório de saída:
            [
              {
                "statement": "Pergunta aqui?",
                "options": ["(A) Opção 1", "(B) Opção 2", "(C) Opção 3", "(D) Opção 4", "(E) Opção 5"],
                "correctAnswerIndex": 1,
                "comment": "Comentário detalhado justificando a resposta"
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
        this.restClient = builder.baseUrl("https://generativelanguage.googleapis.com").defaultHeader("Content-Type", "application/json").build();
    }

    public List<QuestionGenerateDTO> generateQuestions(PromptRequest request) {

        String promptFinal = buildPrompt(request);

        String raw = callGemini(promptFinal);
        String json = extractJsonArray(raw);

        try {
            List<QuestionGenerateDTO> questions = objectMapper.readValue(json, new TypeReference<List<QuestionGenerateDTO>>() {
            });

            validateQuestions(questions);
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
        return PROMPT_MULTIPLE_CHOICE + field("Nível", request.nivel()) + "\nQuantidade: " + request.quantidade() + "\n\nConteúdo base:\n" + request.prompt();
    }

    private String field(String label, String value) {
        return value == null || value.isBlank() ? "" : "\n" + label + ": " + value;
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

        Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

        String response = restClient.post().uri(uriBuilder -> uriBuilder.path("/v1beta/models/gemini-2.5-flash:generateContent").queryParam("key", apiKey).build()).body(body).retrieve().body(String.class);

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

            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText().trim();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta do Gemini", e);
        }
    }

    private void validateQuestions(List<QuestionGenerateDTO> questions) {

        for (QuestionGenerateDTO q : questions) {

            if (q.statement() == null || q.statement().isBlank()) {
                throw new RuntimeException("Questão sem enunciado");
            }
        }
    }
}