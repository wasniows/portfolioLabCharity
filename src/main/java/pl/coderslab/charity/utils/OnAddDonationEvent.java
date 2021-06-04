package pl.coderslab.charity.utils;

import org.springframework.context.ApplicationEvent;
import pl.coderslab.charity.entities.Donation;


public class OnAddDonationEvent extends ApplicationEvent {

    private final Donation donation;

    public OnAddDonationEvent(Donation donation) {
        super(donation);
        this.donation = donation;
    }

    public Donation getDonation() {
        return donation;
    }
}
