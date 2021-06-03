package pl.coderslab.charity.utils;

import lombok.Data;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.coderslab.charity.entities.Donation;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.repositories.DonationRepository;
import pl.coderslab.charity.repositories.UserRepository;

@Data
@Component
public class DonationListener implements ApplicationListener<OnAddDonationEvent> {

    private final JavaMailSender mailSender;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(OnAddDonationEvent event) {
        this.confirmDonation(event);
    }

    private void confirmDonation(OnAddDonationEvent event) {

        //get donation
        Donation donation = event.getDonation();

        //get email properties
        User user = userRepository.findFirstById(donation.getUser().getId());
        String recipientAddress = user.getEmail();
        String subject = "Potwierdzenie zarejestrowania darowizny";
        String message = "Przekazany dar dla " + donation.getInstitution().getName() +
                "\r\n" + "Dzień odbioru: " + donation.getPickUpDate() +
                "\r\n" + "Godzina odbioru: " + donation.getPickUpTime() +
                "\r\n" +
                "\r\n" + "Pozdrawiamy," +
                "\r\n" + "Zespół Projektu \"Oddam w dobre ręce\"";

        //send email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
