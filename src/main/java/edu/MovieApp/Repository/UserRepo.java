package edu.MovieApp.Repository;

import edu.MovieApp.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User, Integer> {

   User findByUsername(String username);
}