package com.ArnabMdev.ArkaneGames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ArnabMdev.ArkaneGames")
public class ArkaneGamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArkaneGamesApplication.class, args);
	}

}
