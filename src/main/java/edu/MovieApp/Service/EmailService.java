package edu.MovieApp.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailService implements EmailImpl{
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private OtpService otpService;


    private String getHtmlEmailContent(String userName, String resetLink, String otp) {
        return "<html>"
                + "<head><style>"
                + "body { background-color: #1a1a1a; color: #ffffff; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; }"
                + ".container { max-width: 600px; margin: auto; background-color: #2b2b2b; padding: 30px; border-radius: 10px; }"
                + ".header { text-align: center; color: #ff0057; font-size: 28px; margin-bottom: 20px; }"
                + ".content { font-size: 16px; line-height: 1.6; }"
                + ".otp { font-size: 24px; font-weight: bold; color: #ff0057; margin-top: 20px; text-align: center; }"
                + ".button { display: inline-block; margin-top: 30px; padding: 15px 25px; font-size: 16px; background-color: #ff0057; color: #ffffff; text-decoration: none; border-radius: 5px; }"
                + ".footer { text-align: center; margin-top: 30px; font-size: 12px; color: #aaaaaa; }"
                + "</style></head>"
                + "<body><div class='container'><div class='header'>SHOWDANE 🎬</div>"
                + "<div class='content'><p>Hello " + userName + ",</p>"
                + "<p>We received a request to reset your password for your SHOWDANE account.</p>"
                + "<p>For security reasons, we’ve sent you a One-Time Password (OTP) to verify your identity:</p>"
                + "<div class='otp'>" + otp + "</div>"
                + "<p>Alternatively, you can reset your password by clicking the button below:</p>"
                + "<p style='text-align: center;'><a href='" + resetLink + "' class='button'>Reset Password</a></p>"
                + "<p>If you didn’t request a password reset, just ignore this email.</p>"
                + "<p>See you soon at SHOWDANE – where movies come to life!</p></div>"
                + "<div class='footer'>&copy; " + java.time.Year.now().getValue() + " SHOWDANE Inc. All rights reserved.</div></div></body></html>";
    }

    @Override
    public ResponseEntity<Map<String, String>> otpSend(Map<String, String> body) {
        String otp = String.valueOf((int)((Math.random() * 900000) + 100000)); // 6-digit OTP

        String link = "http://localhost:4200/reset-password?token=" + otp;

        otpService.saveOtp(body.get("email"), otp);
        String subject = "Reset Your Password - SHOWDANE 🎬";
        String htmlContent = getHtmlEmailContent(body.get("email"), link,otp);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);


            helper.setTo(body.get("email"));
            helper.setFrom("sadewbathiyaa2007@gmail.com");
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
        }
     catch (MessagingException e) {
         Map<String, String> response = new HashMap<>();
         response.put("message", "somthing is wrong, try again");
         return ResponseEntity.status(401).body(response);

    }

        emailSender.send(message);


        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset email sent");
        return ResponseEntity.ok(response);

    }
}
