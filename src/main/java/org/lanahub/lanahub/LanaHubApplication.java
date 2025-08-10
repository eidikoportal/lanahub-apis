package org.lanahub.lanahub;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LanaHubApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // wherever your .env file is
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		SpringApplication.run(LanaHubApplication.class, args);
	}

}
