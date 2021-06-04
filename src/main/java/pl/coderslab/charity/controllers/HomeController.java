package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.entities.Institution;
import pl.coderslab.charity.repositories.DonationRepository;
import pl.coderslab.charity.repositories.InstitutionRepository;

import java.util.Collection;

@Data
@Controller
public class HomeController {

    private final DonationRepository donationRepository;
    private final InstitutionRepository institutionRepository;


    @RequestMapping("/")
    public String homeAction(Model model) {
        model.addAttribute("quantityOfAllBags", donationRepository.quantityOfAllBags());
        model.addAttribute("quantityOfAllDonations", donationRepository.quantityOfAllDonations());
        return "index";
    }

    @ModelAttribute("institutions")
    public Collection<Institution> institutions() {
        return this.institutionRepository.findAll();
    }

}
