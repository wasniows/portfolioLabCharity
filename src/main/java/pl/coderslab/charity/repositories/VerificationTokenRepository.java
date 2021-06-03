package pl.coderslab.charity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.charity.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

    VerificationToken findFirstByToken(String token);
}
