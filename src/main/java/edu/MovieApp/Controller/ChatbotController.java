package edu.MovieApp.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "https://dynamic-chebakia-96e0b5.netlify.app") // Allow Angular frontend to access
public class ChatbotController {

    private final String TMDB_API_KEY = "2c305aa6582edcd8901dcdbb9bb2e9de";
    private final WebClient webClient = WebClient.create("https://api.themoviedb.org/3");

    @PostMapping
    public ResponseEntity<Map<String, String>> chatbotResponse(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message").toLowerCase();

        if (userMessage.contains("movie")) {
            String movieName = userMessage.replace("movie ", "").trim();
            String url = "/search/movie?query=" + movieName + "&api_key=" + TMDB_API_KEY;

            Map<String, Object> response = webClient.get().uri(url).retrieve().bodyToMono(Map.class).block();
            if (response != null && response.containsKey("results")) {
                var results = (java.util.List<Map<String, Object>>) response.get("results");
                if (!results.isEmpty()) {
                    Map<String, Object> movie = results.get(0);
                    String reply = "🎬 *" + movie.get("title") + "* (Released: " + movie.get("release_date") + ")\n"
                            + "⭐ Rating: " + movie.get("vote_average") + "/10\n"
                            + "📖 Overview: " + movie.get("overview");
                    return ResponseEntity.ok(Map.of("reply", reply));
                }
            }
            return ResponseEntity.ok(Map.of("reply", "Sorry, I couldn't find that movie."));
        } else {
            return ResponseEntity.ok(Map.of("reply", "I'm a movie chatbot! Ask me about any movie 🎥"));
        }
    }
}


