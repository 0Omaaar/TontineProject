package com.tontine;

import com.tontine.modele.Utilisateur;
import com.tontine.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TontineApplication implements CommandLineRunner {
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	public static void main(String[] args) {
		SpringApplication.run(TontineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNomComplet("Yassir el haj");
		utilisateur.setEmail("Yassir@elhaj.com");
		utilisateur.setCin("AYB1888");
		utilisateur.setMotDePasse("YassirP@ssw0rdelhaj");
		utilisateur.setRole(Utilisateur.Role.USER);
		utilisateur.setNumTele("0666666666");
		utilisateurRepository.save(utilisateur);
	}
}
