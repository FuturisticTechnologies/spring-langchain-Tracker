package com.proj.ai;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

	private final Assistant assistant;

    // Spring AI 0.8.1 automatically registers a ChatClient bean for Mistral
    public ChatController(Assistant assistant) {
        this.assistant = assistant;
    }
    
    @PostMapping("/api/chat")
    public ResponseEntity<Map<String, String>> handleChatRequest(@RequestBody Map<String, String> payload) {
        
        // 1. Extract the text user prompt sent by Streamlit's JSON payload
        String userPrompt = payload.get("message");
        
        // Fallback default message handling if input text is empty
        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("reply", "Please provide a valid question.");
            return ResponseEntity.badRequest().body(errorBody);
        }
        
        try {
            // 2. Pass the prompt to the pre-compiled LangChain4j dynamic proxy
            // This single line triggers your Vector DB search and Mistral API call
            String aiResponse = this.assistant.chat(userPrompt);

            // 3. Construct the response payload matching what Streamlit expects
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("reply", aiResponse);
            
            //this below sysout mention that this is the virtual thread because of we have added the configuration
            //at application.properties file, if not added it will be normal thread.
            System.out.println("🤖 Handling request on thread: " + Thread.currentThread());
            
            // 4. Return the native map with an HTTP 200 OK status
            return ResponseEntity.ok(responseBody);
            
        } catch (Exception e) {
            // Fallback runtime error handling
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("reply", "Error processing request in Java backend: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorBody);
        }
    }
}
