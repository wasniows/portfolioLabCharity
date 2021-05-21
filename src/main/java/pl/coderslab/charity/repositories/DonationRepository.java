package pl.coderslab.charity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entities.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
}
