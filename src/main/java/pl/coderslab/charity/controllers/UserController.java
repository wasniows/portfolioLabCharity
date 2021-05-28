package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entities.Authority;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.repositories.AuthorityRepository;
import pl.coderslab.charity.repositories.UserRepository;

import javax.validation.Valid;

@Data
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;


    @GetMapping("/user")
        public String User(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        User user = userRepository.findFirstByEmail(email);
        model.addAttribute("username", user.getFirstName());
        return "userPage";
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
        userRepository.save(user);

        Authority authority = new Authority(user.getEmail(), "ROLE_USER");
        authorityRepository.save(authority);

        return "register-confirmation";

    }

}
