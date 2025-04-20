package edu.MovieApp.modal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountResponse {
    private boolean isEmailDuplicate=false;
    private boolean isUserNameDuplicate=false;

}
