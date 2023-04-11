package fr.real.supervision.appliinfo.web.ldap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;





@Controller
public class InformationController {

    @GetMapping("/information/{statut}")
   public String viewPost(@PathVariable("statut") String statut, Model model) {

        model.addAttribute("statut", statut);

       return "information";
   }

}

