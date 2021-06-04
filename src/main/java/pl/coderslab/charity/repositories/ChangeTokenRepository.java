package pl.coderslab.charity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entities.ChangeToken;

@Repository
public interface ChangeTokenRepository extends JpaRepository<ChangeToken, String> {

    ChangeToken findFirstByToken(String token);
}
