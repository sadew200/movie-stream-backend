package edu.MovieApp.modal;


import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "userInfo")
public class Account {
    @Id
    private String email;
    @Column(unique = true,nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    private String country;

    public Account(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
