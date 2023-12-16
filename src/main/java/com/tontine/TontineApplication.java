package com.tontine;

import com.tontine.entities.User;
import com.tontine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TontineApplication implements CommandLineRunner {
	@Autowired
	private UserRepository UserRepository;

	public static void main(String[] args) {
		SpringApplication.run(TontineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		User User = new User();
//		User.setNomComplet("Yassir el haj");
//		User.setEmail("Yassir@elhaj.com");
//		User.setCin("AYB1888");
//		User.setMotDePasse("YassirP@ssw0rdelhaj");
//		User.setRole(User.Role.USER);
//		User.setNumTele("0666666666");
//		UserRepository.save(User);
	}
}
