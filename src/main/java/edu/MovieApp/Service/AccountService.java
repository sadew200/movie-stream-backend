package edu.MovieApp.Service;

import edu.MovieApp.Repository.AccountRepository;
import edu.MovieApp.modal.Account;
import edu.MovieApp.modal.AccountResponse;
import edu.MovieApp.modal.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountImpl{
    public static String email="";

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    JWTService jwtService;
    @Autowired
    HttpServletResponse response;
    private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);
    public ResponseEntity<LoginResponse> login(Account account, HttpServletResponse response) {
        LoginResponse loginResponse=new LoginResponse();
        if(accountRepository.existsById(account.getEmail())){
            loginResponse.setExistsEmail(true);
            Optional<Account> byId = accountRepository.findById(account.getEmail());
            Account user=byId.get();
            if (bCryptPasswordEncoder.matches(account.getPassword(), user.getPassword())) {
                loginResponse.setExistsPassword(true);
            }


        }
        System.out.println(loginResponse);
        if(loginResponse.isExistsEmail() && loginResponse.isExistsPassword()){
            String jwtToken=jwtService.generateToken(account.getEmail());
            String cookieValue = "jwt=" + jwtToken +
                    "; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=86400";

            response.setHeader("Set-Cookie", cookieValue);

            System.out.println(loginResponse);
           return ResponseEntity.ok(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
    }


    public ResponseEntity<AccountResponse> register(Account account) {
        AccountResponse accountResponse=new AccountResponse();
        if(accountRepository.existsById(account.getEmail())){
            accountResponse.setEmailDuplicate(true);
        }
        if(accountRepository.existsByUserName(account.getUserName())){
            accountResponse.setUserNameDuplicate(true);
        }
        else if(accountResponse.isEmailDuplicate()==false && accountResponse.isUserNameDuplicate()==false){
            account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
            return ResponseEntity.ok(accountResponse);
        }
        return ResponseEntity.status(401).body(accountResponse);

    }

    @Override
    public ResponseEntity<String> changePassword(String password) {
        try {
            Optional<Account> account = accountRepository.findById(email);
            Account acc = account.get();
            acc.setPassword(bCryptPasswordEncoder.encode(password));
            accountRepository.save(acc);
        }
        catch (Exception e){
            ResponseEntity.status(401).body("somthing went wrong");
        }
        return ResponseEntity.ok("changed");
    }
}
