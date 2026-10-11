package com.example.QuickFixersBackend.services.serviceImpl;
import com.example.QuickFixersBackend.services.serviceInterfce.ChatbotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import java.util.List;

@Service
public class ChatbotServiceImpl implements ChatbotService {
    @Value("${gemini.api-key}")
    private String apiKey;
    private final Resource guideFile = new ClassPathResource("quickfixers-guide.txt");

    private String readGuide() throws IOException {
        return guideFile.getContentAsString(StandardCharsets.UTF_8);
    }

    private RestClient createClient() {
        return RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    private Map<String, Object> createBody(String message)
            throws IOException {

        return Map.of(
                "model", "gemini-3.8-flash",
                "system_instruction", readGuide(),
                "input", message,
                "store", false
        );
    }

    private Map<String, Object> sendQuestion(String message)
            throws IOException {

        return createClient()
                .post()
                .uri("/interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(createBody(message))
                .retrieve()
                .body(new ParameterizedTypeReference<
                        Map<String, Object>>() {});
    }

    private String extractAnswer(Map<String, Object> response) {
        StringBuilder answer = new StringBuilder();

        if (response != null
                && response.get("steps") instanceof List<?> steps) {

            for (Object item : steps) {
                if (item instanceof Map<?, ?> step
                        && "model_output".equals(step.get("type"))
                        && step.get("content") instanceof List<?> content) {

                    for (Object part : content) {
                        if (part instanceof Map<?, ?> block
                                && "text".equals(block.get("type"))
                                && block.get("text") instanceof String text) {

                            answer.append(text);
                        }
                    }
                }
            }
        }

        return answer.toString();
    }

    @Override
    public String reply(String message) throws IOException {
        Map<String, Object> response = sendQuestion(message);
        String answer = extractAnswer(response);

        if (answer.isBlank()) {
            return "Sorry, I couldn't generate an answer. Please try again.";
        }

        return answer;
    }
}
