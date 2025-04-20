package edu.MovieApp.Repository;


import edu.MovieApp.modal.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
    boolean existsByUserName(String userName);
}
