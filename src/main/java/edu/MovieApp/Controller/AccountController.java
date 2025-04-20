package edu.MovieApp.Controller;

import edu.MovieApp.Service.AccountImpl;
import edu.MovieApp.Service.JWTService;
import edu.MovieApp.modal.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    AccountImpl accountImpl;

    @Autowired
    JWTService jwtService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @RequestBody Account account){
        return accountImpl.login(account,response);
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


}
