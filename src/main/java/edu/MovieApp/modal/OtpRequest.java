package edu.MovieApp.modal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpRequest {
    private String email;
    private String otp;
}
