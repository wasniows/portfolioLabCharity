package pl.coderslab.charity.utils;

import lombok.Data;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.services.UserService;

import java.util.UUID;

@Data
@Component
public class UserListener implements ApplicationListener<OnCreatedUserEvent> {

    private String serverUrl = "http://localhost:8080";

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Override
    public void onApplicationEvent(OnCreatedUserEvent event) {
        this.confirmCreateUser(event);
    }

    private void confirmCreateUser(OnCreatedUserEvent event) {

        //get user
        User user = event.getUser();

        //create verification token
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        //get email properties
        String recipientAddress = user.getEmail();
        String subject = "Aktywacja konta w serwisie \"Oddam w dobre ręce\"";
        String confirmationUrl = event.getAppUrl() + "registerConfirm?token=" + token;
        String message = "Link aktywujący nowe konto:";

        //send email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);
    }
}
