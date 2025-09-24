package com.example.Kiddit.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.Kiddit.DataTransferObject.OpenAiResponse;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GptService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    public GptService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        // Initialize WebClient with the base URL
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .filter(logRequest())  
                .filter(logResponse()) 
                .build();        
        this.objectMapper = objectMapper;
    }

    /**
     * Sends the comment content to GPT for appropriateness checking and returns modification suggestions.
     *
     * @param commentContent The content of the comment to be checked
     * @return The response from GPT about the appropriateness of the comment
     */
    public String chatWithGpt(String commentContent) {
        // Fixed prompt template with dynamic insertion of comment content
        String prompt = """
            Please check the appropriateness of the following comment and classify it into one of the following categories:
            - "Offensive"
            - "Hate Speech"
            - "Spam"
            - "Inappropriate"
            - "Appropriate"

            If the comment is inappropriate, return a JSON response with two fields:
            - "label": the category of the issue ("Offensive", "Hate Speech", "Spam", "Inappropriate")
            - "suggestion": a suggestion for how to modify the comment
            If the comment is appropriate, return a JSON response with two fields:
            - "label": "Appropriate"
            - "suggestion": ""
            The comment is as follows:
            %s
        """.formatted(commentContent);  // Dynamically insert the comment content into the prompt

        // Create the request body object
        RequestBody requestBody = new RequestBody(model, prompt, 0.2);

        // Use ObjectMapper to convert the request body object to a JSON string
        String requestBodyJson = null;
        try {
            requestBodyJson = objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error serializing request body.";
        }

        // Send the request to OpenAI API and retrieve the response
        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBodyJson)  // Use the JSON string as the request body
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    return response.bodyToMono(String.class).flatMap(body -> {
                        System.out.println("Error response body: " + body);  // Log the error response body
                        return Mono.error(new Exception("Error from GPT API: " + body));
                    });
                })
                .bodyToMono(OpenAiResponse.class)
                .map(response -> response.getChoices().get(0).getMessage().getContent())  // Extract GPT's response content
                .block();  // Block and get the response
    }

    /**
     * Class representing the request body for the API call, containing the model, messages, and temperature.
     */
    static class RequestBody {
        private String model;
        private Message[] messages;
        private double temperature;

        public RequestBody(String model, String prompt, double temperature) {
            this.model = model;
            this.messages = new Message[]{new Message("user", prompt)};
            this.temperature = temperature;
        }

        public String getModel() {
            return model;
        }

        public Message[] getMessages() {
            return messages;
        }

        public double getTemperature() {
            return temperature;
        }
    }

    /**
     * Class representing a message, containing the role and content of the message.
     */
    static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }

    // debug GPT-API request and response
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("Response: " + clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientResponse);
        });
    }
}
