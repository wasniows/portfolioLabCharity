package pl.coderslab.charity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email);
    User findFirstById(Long userId);
}
