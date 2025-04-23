package edu.MovieApp.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EmailImpl {
     ResponseEntity<Map<String, String>> otpSend(Map<String, String> body);
}
