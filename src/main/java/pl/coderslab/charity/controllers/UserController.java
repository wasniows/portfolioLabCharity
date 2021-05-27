package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entities.Authority;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.repositories.AuthorityRepository;
import pl.coderslab.charity.repositories.UserRepository;

@Data
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
//    private final PasswordEncoder encoder;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String addUser(User user) {

        Authority authority = new Authority(user.getEmail(),"ROLE_USER");
        authorityRepository.save(authority);
//        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return "register-confirmation";

    }

}
