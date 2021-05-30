package pl.coderslab.charity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entities.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findFirstByEmail(String email);

}
