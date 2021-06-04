package pl.coderslab.charity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entities.ResetToken;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {

    ResetToken findFirstByToken(String token);
}
