package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entities.Category;
import pl.coderslab.charity.entities.Donation;
import pl.coderslab.charity.entities.Institution;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.repositories.CategoryRepository;
import pl.coderslab.charity.repositories.DonationRepository;
import pl.coderslab.charity.repositories.InstitutionRepository;
import pl.coderslab.charity.repositories.UserRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Controller
public class DonationController {

    private final CategoryRepository categoryRepository;
    private final InstitutionRepository institutionRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    @GetMapping("/donations")
    public String myDonations(@AuthenticationPrincipal UserDetails userDetails ,Model model){
        String email = userDetails.getUsername();
        User user = userRepository.findFirstByEmail(email);
        List<Donation> myDonations = donationRepository.myDonations(user.getId());
        model.addAttribute("myDonations", myDonations);
        return "donations";
    }

    @GetMapping("/donationDetails/{id}")
    public String donationDetails(@PathVariable Long id, Model model){
        Donation donation = donationRepository.findFirstById(id);
        model.addAttribute("donation", donation);
        return "donationDetails";
    }

    @GetMapping("/donation/confirmation/{id}")
    public String donationComfirmation(@PathVariable Long id){
        Donation donation = donationRepository.findFirstById(id);
        LocalDate localDate = LocalDate.now();
        donation.setConfirmDate(localDate);
        donation.setReceived(true);
        donationRepository.save(donation);
        return "redirect:/donations";
    }

    @GetMapping("/form")
    public String formDonation(Model model) {
        model.addAttribute("donation", new Donation());
        return "form";
    }

    @PostMapping("/form")
    public String addDonation(@Valid Donation donation, BindingResult result, @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {

        if (result.hasErrors()){
            model.addAttribute("donation", donation);
            return "form";
        }

        String email = userDetails.getUsername();
        User user = userRepository.findFirstByEmail(email);
        donation.setUser(user);
        donation.setReceived(false);
        donationRepository.save(donation);
        return "form-confirmation";
    }

    @ModelAttribute("institutions")
    public Collection<Institution> institutions() {
        return this.institutionRepository.findAll();
    }

    @ModelAttribute("categories")
    public Collection<Category> categories() {
        return this.categoryRepository.findAll();
    }
}
