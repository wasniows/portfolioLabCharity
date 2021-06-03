package pl.coderslab.charity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.entities.Donation;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    Donation findFirstById(Long donationId);

    @Query("SELECT SUM(d.quantity) FROM Donation d")
    public Integer quantityOfAllBags();

    @Query("SELECT COUNT(d) FROM Donation d")
    public Integer quantityOfAllDonations();

    @Query(value = "SELECT * FROM donations WHERE user_id = :userId", nativeQuery = true)
    List<Donation> myDonations(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM donations WHERE institution_id = :institutionId", nativeQuery = true)
    List<Donation> donationsByInstitutionId(@Param("institutionId") Long institutionId);
}
