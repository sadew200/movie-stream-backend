package edu.MovieApp.modal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Login {
    private String email;
    private String password;
}
