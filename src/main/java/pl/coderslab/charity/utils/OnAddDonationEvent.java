package pl.coderslab.charity.utils;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import pl.coderslab.charity.entities.Donation;

@Data
public class OnAddDonationEvent extends ApplicationEvent {

    private final Donation donation;

    public OnAddDonationEvent(Donation donation) {
        super(donation);
        this.donation = donation;
    }
}
