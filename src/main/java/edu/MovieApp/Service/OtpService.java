package edu.MovieApp.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private static final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    public void saveOtp(String email, String otp) {
        otpStorage.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
    }

    public boolean validateOtp(String email, String otp) {
        OtpData data = otpStorage.get(email);

        if (data == null) return false;
        if (data.getExpiry().isBefore(LocalDateTime.now())) {
            otpStorage.remove(email); // Clean up expired
            return false;
        }

        boolean isValid = data.getOtp().equals(otp);
        if (isValid){
            AccountService.email=email;
            otpStorage.remove(email);
        }; // One-time use

        return isValid;
    }

    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiry;

        public OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        public String getOtp() { return otp; }
        public LocalDateTime getExpiry() { return expiry; }
    }
}
