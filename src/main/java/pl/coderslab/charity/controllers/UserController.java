package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entities.Authority;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.models.Password;
import pl.coderslab.charity.repositories.AuthorityRepository;
import pl.coderslab.charity.repositories.UserRepository;

import javax.validation.Valid;
import java.util.Collection;

@Data
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;

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

        if (!user.getEmail().equals(userRepository.findFirstById(user.getId()).getEmail())) {
            if (userRepository.findFirstByEmail(user.getEmail()) != null) {
                result.rejectValue("email", "error.user", "Taki email już istnieje");
                model.addAttribute("user", user);
                return "editUser";
            }
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
        User user = userRepository.findFirstByEmail(email);
        model.addAttribute("username", user.getFirstName());

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

        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setAccess(true);
        Authority authority = new Authority(user.getEmail(), "USER");
        authorityRepository.save(authority);
        user.setAuthority(authorityRepository.findFirstByEmail(user.getEmail()));
        userRepository.save(user);

        return "register-confirmation";

    }

    @ModelAttribute("users")
    public Collection<User> users() {
        return this.userRepository.findAll();
    }

}
