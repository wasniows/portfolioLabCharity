package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.entities.ResetToken;
import pl.coderslab.charity.models.Password;
import pl.coderslab.charity.repositories.ResetTokenRepository;
import pl.coderslab.charity.repositories.UserRepository;
import pl.coderslab.charity.services.PasswordService;
import pl.coderslab.charity.utils.OnPasswordResetEvent;

import javax.validation.Valid;

@Data
@Controller
public class PasswordController {

    private final ApplicationEventPublisher eventPublisher;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder encoder;
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @GetMapping("/password")
    public String resetPasswordForm(@ModelAttribute("password") Password password){
        return "password";
    }

    @PostMapping("/password")
    public String emailToRestPassword(@Valid Password password,
                                      BindingResult result, Model model){
        //check for errors
        if (result.hasErrors()){
            return "password";
        }

        //verify email from datebase
        if (userRepository.findFirstByEmail(password.getEmail()) == null) {
            result.rejectValue("email", "error.user", "Taki email nie istnieje");
            model.addAttribute("password", password);
            return "password";
        }

        //fire off an event to reset email
        eventPublisher.publishEvent(new OnPasswordResetEvent(password, "/"));
        return "passwordSendMail";

    }

    @GetMapping("/passwordReset")
    public String getNewPassword(@RequestParam("token") String token, Model model){

        //verify token
        ResetToken resetToken = resetTokenRepository.findFirstByToken(token);
        Password password = new Password();
        password.setEmail(resetToken.getEmail());
        password.setToken(token);
        model.addAttribute("password", password);
        return "resetPassword";
    }

    @PostMapping("/passwordReset")
    public String seveNewPassword(@Valid Password password, BindingResult result){

        //check for errors
        if(result.hasErrors()){
            return "resetPassword";
        }

        //math the passwords
        if (!password.getNewPassword().equals(password.getMatchingNewPassword())) {
            result.rejectValue("password", "error.user", "Wpisane hasła są różne");
            return "resetPassword";
        }

        //save new password
        password.setNewPassword(encoder.encode(password.getNewPassword()));
        passwordService.update(password);
        return "resetPasswordConfirm";
    }
}
