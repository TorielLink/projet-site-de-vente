package com.clemclo.projetsitevente.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author mourad.ouziri
 * @date 2023
 *
 */

@Entity
@Table (name = "TClients")
public class Client {
	@Id @GeneratedValue
	private Long numero;	
	private String nom;
	private String adresse;
	
	public Client() {}

	public Client(String nom, String adresse) {
		this.nom = nom;
		this.adresse = adresse;
	}
	
	public Long getNumero() {
		return numero;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getAdresse() {
		return adresse;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}	
}
