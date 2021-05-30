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

    @RequestMapping("admin/userAccess/{id}")
    public String userAccess(@PathVariable long id) {
        User user = userRepository.findFirstById(id);
        boolean access = user.isAccess();
        access = !access;
        user.setAccess(access);
        userRepository.save(user);
        return "redirect:/user";
    }

    @RequestMapping("admin/userAuthority/{id}")
    public String userAuthority(@PathVariable long id) {
        User user = userRepository.findFirstById(id);
        Authority authority = authorityRepository.findFirstByEmail(user.getEmail());
        String authorityName = authority.getAuthority();
        if (authorityName.equals("ADMIN")){
            authorityName = "USER";
        }else {
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
