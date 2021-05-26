package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entities.Category;
import pl.coderslab.charity.entities.Donation;
import pl.coderslab.charity.entities.Institution;
import pl.coderslab.charity.repositories.CategoryRepository;
import pl.coderslab.charity.repositories.DonationRepository;
import pl.coderslab.charity.repositories.InstitutionRepository;

import java.util.Collection;

@Data
@Controller
public class DonationController {

    private final CategoryRepository categoryRepository;
    private final InstitutionRepository institutionRepository;
    private final DonationRepository donationRepository;

    @GetMapping("/form")
    public String formDonation(Model model) {
        model.addAttribute("donation", new Donation());
        return "form";
    }

    @PostMapping("/form")
    public String addDonation(Donation donation) {
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
