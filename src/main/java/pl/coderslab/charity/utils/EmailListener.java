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
public class EmailListener implements ApplicationListener<OnChangeEmailEvent> {

    private String serverUrl = "http://localhost:8080";

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Override
    public void onApplicationEvent(OnChangeEmailEvent event) {
        this.changeEmail(event);
    }

    private void changeEmail(OnChangeEmailEvent event){

        //create email token
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createChangeEmailToken(user, token);

        //get email properties
        String recipientAddress = user.getChangeToken().getNewEmail();
        String subject = "Zmiana adresu Email dla konta na portalu \"Oddam w dobre ręce\"";
        String confirmationUrl = event.getAppUrl() + "emailChange?token=" + token;
        String message = "Link aktywujący zmianę adresu email:";

        //send email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);

    }
}

