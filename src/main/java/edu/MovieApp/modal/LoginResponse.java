package edu.MovieApp.modal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class LoginResponse {
    private boolean isExistsEmail=false;
    private boolean isExistsPassword=false;
}
