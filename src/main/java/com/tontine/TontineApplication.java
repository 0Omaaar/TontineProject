package com.tontine;


import com.tontine.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TontineApplication{

	public static void main(String[] args) {
		SpringApplication.run(com.tontine.TontineApplication.class, args);
	}

}
