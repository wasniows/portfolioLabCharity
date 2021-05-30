package pl.coderslab.charity.controllers;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entities.Institution;
import pl.coderslab.charity.repositories.InstitutionRepository;

import javax.validation.Valid;
import java.util.Collection;

@Data
@Controller
public class InstitutionController {

    private final InstitutionRepository institutionRepository;

    @RequestMapping("/admin/institutions")
    public String getInstitution(){
        return "institutions";
    }

    @GetMapping("/admin/institution/add")
    public String addInstitution(Model model){
        model.addAttribute("institution", new Institution());
        return "addInstitution";
    }

    @PostMapping("/admin/institution/add")
    public String saveInstitution(@Valid Institution institution, BindingResult result){

        if (result.hasErrors()){
            return "addInstitution";
        }
        institutionRepository.save(institution);
        return "redirect:/admin/institutions";
    }

    @RequestMapping("/admin/institution/del/{id}")
    public String deleteInstitution(@PathVariable long id){
        Institution institution = institutionRepository.findFirstById(id);
        institutionRepository.delete(institution);
        return "redirect:/admin/institutions";
    }

    @RequestMapping("/admin/institution/edit/{id}")
    public String editFormInstitution(@PathVariable long id, Model model){
        Institution institution = institutionRepository.findFirstById(id);
        model.addAttribute("institution", institution);
        return "editInstitution";
    }

    @RequestMapping("/admin/institution/edit")
    public String editInstitution(@Valid Institution institution, BindingResult result){
        if (result.hasErrors()){
            return "editInstitution";
        }
        institutionRepository.save(institution);
        return "redirect:/admin/institutions";
    }


    @ModelAttribute("institutions")
    public Collection<Institution> institutions() {
        return this.institutionRepository.findAll();
    }
}
