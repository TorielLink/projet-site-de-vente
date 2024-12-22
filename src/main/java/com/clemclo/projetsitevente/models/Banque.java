package com.clemclo.projetsitevente.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import banque.persistance.IPersistanceBanque;

// Exemple de modèle
@Service
public class Banque implements IBanque {
	
	@Autowired
	IPersistanceBanque peristance;
	
	public Client ajouterClient(String nom, String adresse) {
		Client c = new Client (nom, adresse);
		Client clientStocke = peristance.stockerNouveaunClient(c);
		return clientStocke;
	}
	
}
