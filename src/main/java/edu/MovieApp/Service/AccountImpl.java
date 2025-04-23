package edu.MovieApp.Service;

import edu.MovieApp.modal.Account;
import edu.MovieApp.modal.AccountResponse;
import edu.MovieApp.modal.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AccountImpl {
    ResponseEntity<LoginResponse> login(Account account, HttpServletResponse response);

    ResponseEntity<AccountResponse> register(Account account);

    ResponseEntity<String> changePassword(String password);
}
