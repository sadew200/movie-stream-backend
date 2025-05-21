package edu.MovieApp.Controller;

import edu.MovieApp.Service.AccountImpl;
import edu.MovieApp.Service.EmailImpl;
import edu.MovieApp.Service.JWTService;
import edu.MovieApp.Service.OtpService;
import edu.MovieApp.modal.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    AccountImpl accountImpl;
    @Autowired
    EmailImpl emailImpl;
    @Autowired
    private OtpService otpService;




    @Autowired
    JWTService jwtService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @RequestBody Account account){
        return accountImpl.login(account,response);
    }
    @GetMapping("/home")
    public String home() {
        return "App is alive";
    }


    @PostMapping("/validate")
    public ResponseEntity<String> validate(HttpServletRequest request) {
        String token = null;

        // Get the cookie named "jwt"
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(401).body("No token found in cookies");
        }

        String username = jwtService.extractUserName(token);

        if (username != null && jwtService.validateToken(token, username)) {
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.status(401).body("Invalid token");
    }

    @PostMapping("register")
    public ResponseEntity<AccountResponse> register(@RequestBody Account account){
        return accountImpl.register(account);
    }
    @GetMapping("getReq")
    public String getReq(){
        return "welcome";
    }

    @PostMapping("forgotPassword")
    public  ResponseEntity<Map<String, String>> forgetPassword(@RequestBody Map<String, String> body) throws MessagingException {
        return emailImpl.otpSend(body);

    }
    @GetMapping("logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        String cookieValue = "jwt=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0";
        response.setHeader("Set-Cookie", cookieValue);
        return ResponseEntity.ok("Logged out successfully.");
    }


    @PostMapping("verifyOtp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody OtpRequest request) {
        System.out.println(request);
        boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());

        if (isValid) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP is valid!");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired OTP.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @PostMapping("changePassword")
    public  ResponseEntity<String> changePassword(@RequestBody Map<String, String> body){
        return accountImpl.changePassword(body.get("password"));
    }


}
