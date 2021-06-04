package pl.coderslab.charity.utils;

import lombok.Data;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.coderslab.charity.models.Password;
import pl.coderslab.charity.services.PasswordService;

import java.util.UUID;

@Data
@Component
public class PasswordListener implements ApplicationListener<OnPasswordResetEvent> {

    private String serverUrl = "http://localhost:8080";

    private final JavaMailSender mailSender;
    private final PasswordService passwordService;

    @Override
    public void onApplicationEvent(OnPasswordResetEvent event) {
        this.resetPassword(event);
    }

    private void resetPassword(OnPasswordResetEvent event) {

        //create password token
        Password password = event.getPassword();
        String token = UUID.randomUUID().toString();
        passwordService.createResetToken(password, token);

        //get email properties
        String recipientAddress = password.getEmail();
        String subject = "Zmiana hasła dla konta na \"Oddam w dobre ręce\"";
        String confirmationUrl = event.getAppUrl() + "passwordReset?token=" + token;
        String message = "Link aktywujący zmianę hasła:";

        //send email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);
    }
}
