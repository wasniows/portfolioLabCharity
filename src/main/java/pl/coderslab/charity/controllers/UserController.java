package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entities.Authority;
import pl.coderslab.charity.entities.ChangeToken;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.entities.VerificationToken;
import pl.coderslab.charity.models.Password;
import pl.coderslab.charity.repositories.AuthorityRepository;
import pl.coderslab.charity.repositories.ChangeTokenRepository;
import pl.coderslab.charity.repositories.UserRepository;
import pl.coderslab.charity.repositories.VerificationTokenRepository;
import pl.coderslab.charity.services.UserService;
import pl.coderslab.charity.utils.OnChangeEmailEvent;
import pl.coderslab.charity.utils.OnCreatedUserEvent;

import javax.validation.Valid;
import java.util.Collection;

@Data
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ChangeTokenRepository changeTokenRepository;

    @RequestMapping("/user/edit/{id}")
    public String editUserForm(@PathVariable long id, Model model) {
        User user = userRepository.findFirstById(id);
        model.addAttribute("user", user);
        return "editUser";
    }

    @GetMapping("/user/edit")
    public String editUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "editUser";
        }

        User userToChange = userRepository.findFirstById(user.getId());
        Authority authority = authorityRepository.findFirstByEmail(userToChange.getEmail());
        authority.setEmail(user.getEmail());
        authorityRepository.save(authority);

        userToChange.setEmail(user.getEmail());
        userToChange.setFirstName(user.getFirstName());
        userToChange.setLastName(user.getLastName());
        userRepository.save(userToChange);

        return "changeUser-confirmation";
    }

    @RequestMapping("/emailChange/{id}")
    public String ChangeEmailForm(@PathVariable long id, Model model) {
        User user = userRepository.findFirstById(id);
        ChangeToken changeToken = new ChangeToken();
        changeToken.setEmail(user.getEmail());
        model.addAttribute("changeToken", changeToken);
        return "editEmail";
    }

    @PostMapping("/emailChange")
    public String changeEmail(@Valid ChangeToken changeToken, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "editEmail";
        }

        if (userRepository.findFirstByEmail(changeToken.getNewEmail()) != null) {
            result.rejectValue("newEmail", "error.changeToken", "Taki email już istnieje");
            model.addAttribute("changeToken", changeToken);
            return "editEmail";
        }

        User user = userRepository.findFirstByEmail(changeToken.getEmail());
        user.setChangeToken(changeToken);
        userRepository.save(user);

        //send change email confirmation
        eventPublisher.publishEvent(new OnChangeEmailEvent(user, "/"));

        return "changeEmailSend";
    }

    @GetMapping("/emailChange")
    public String changeEmainConfirm(@RequestParam("token") String token){
            ChangeToken changeToken = changeTokenRepository.findFirstByToken(token);
            userService.confirmChangeEmail(changeToken.getToken());
        return "changeEmail-confirmation";
    }

    @RequestMapping("/user/changePassword/{id}")
    public String changePasswordForm(@PathVariable long id, Model model) {
        User user = userRepository.findFirstById(id);
        Password password = new Password();
        password.setEmail(user.getEmail());
        model.addAttribute("password", password);
        return "changePassword";
    }

    @GetMapping("/user/changePassword")
    public String changePassword(@Valid Password password, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "changePassword";
        }

        User user = userRepository.findFirstByEmail(password.getEmail());
        if (!encoder.matches(password.getPassword(), user.getPassword())) {
            result.rejectValue("password", "error.password", "Błędne aktualne hasło");
            model.addAttribute("password", password);
            return "changePassword";
        }

        if (!password.getNewPassword().equals(password.getMatchingNewPassword())) {
            result.rejectValue("email", "error.password", "Błąd potwierdzenia nowego hasła");
            model.addAttribute("password", password);
            return "changePassword";
        }

        user.setPassword(encoder.encode(password.getNewPassword()));
        userRepository.save(user);
        return "changePassword-confirmation";
    }


    @RequestMapping("/user/profil")
    public String userProfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        User user = userRepository.findFirstByEmail(email);
        model.addAttribute("user", user);
        return "profil";
    }

    @RequestMapping("/admin/userAccess/{id}")
    public String userAccess(@PathVariable long id) {
        User user = userRepository.findFirstById(id);
        boolean access = user.isAccess();
        access = !access;
        user.setAccess(access);
        userRepository.save(user);
        return "redirect:/user";
    }

    @RequestMapping("/admin/userAuthority/{id}")
    public String userAuthority(@PathVariable long id) {
        User user = userRepository.findFirstById(id);
        Authority authority = authorityRepository.findFirstByEmail(user.getEmail());
        String authorityName = authority.getAuthority();
        if (authorityName.equals("ADMIN")) {
            authorityName = "USER";
        } else {
            authorityName = "ADMIN";
        }
        authority.setAuthority(authorityName);
        authorityRepository.save(authority);
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String User(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        String role = authorityRepository.findFirstByEmail(email).getAuthority();
        String firstName = userRepository.findFirstByEmail(email).getFirstName();
        model.addAttribute("firstName", firstName);

        if (role.equals("ADMIN")) {
            return "admin";
        }
        return "user";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult result, Model model) {

        //check for errors
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        //verify that the email don't already exist
        if (userRepository.findFirstByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "error.user", "Taki email już istnieje");
            model.addAttribute("user", user);
            return "register";
        }

        //check matching passwords
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            result.rejectValue("password", "error.user", "Podane hasła są różne");
            model.addAttribute("user", user);
            return "register";
        }

        //create user
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(false);
        user.setAccess(true);
        user = userService.create(user);

        //send email account confirmation
        eventPublisher.publishEvent(new OnCreatedUserEvent(user, "/"));
        return "registerSendMail";

    }

    @GetMapping("registerConfirm")
    public String registerConfirm(@RequestParam("token") String token) {
        VerificationToken verificationToken = verificationTokenRepository.findFirstByToken(token);
        userService.confirmUser(verificationToken.getToken());
        return "register-confirmation";
    }

    @ModelAttribute("users")
    public Collection<User> users() {
        return this.userRepository.findAll();
    }

}
