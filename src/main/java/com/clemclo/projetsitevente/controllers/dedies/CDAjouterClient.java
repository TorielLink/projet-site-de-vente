package com.clemclo.projetsitevente.controllers.dedies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import banque.modele.Client;
import banque.modele.IBanque;

@Controller
public class CDAjouterClient {
	
	@Autowired
	private IBanque banque;
	
	@RequestMapping(path="/AjouterClient")
	public String ajouterNouveauClient (ModelMap pModel, @RequestParam("pNom") String nom, @RequestParam("pAdresse") String adresse) {
		// 1. Appeler le modèle
		Client client = banque.ajouterClient(nom, adresse);
		// 2. Transmettre le résultat du modèle à la Vue
		pModel.addAttribute("ClientAjoute", client);
		// 3. Revoyer le nom de la Vue qui affichera le résultat
		return "ConfirmationAjoutClient";
	}

}
