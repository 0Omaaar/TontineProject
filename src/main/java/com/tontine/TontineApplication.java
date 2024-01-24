package com.tontine;

import com.tontine.entities.User;
import com.tontine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class TontineApplication
{
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(TontineApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		User user = new User();
//		user.setNom_prenom("Yassir el haj");
//		user.setEmail("Yassir@elhaj.com");
//		user.setCin("AYB1888");
//		user.setPassword("YassirP@ssw0rdelhaj");
//		user.setRoles("USER");
//		user.setNumTele("0666666666");
//		userRepository.save(user);
//	}
}
