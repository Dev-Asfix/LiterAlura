package com.alura.literatura;

import com.alura.literatura.principal.Principal;
import com.alura.literatura.repository.SerieRepository;
import com.alura.literatura.service.ConsumoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner{

	@Autowired
	private SerieRepository repository;

	public static void main(String[] args) {

		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		Principal principal = new Principal(repository);
		principal.menu();
	}


}
