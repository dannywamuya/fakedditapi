package me.dwamuya.fakeddit;

import me.dwamuya.fakeddit.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class FakedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakedditApplication.class, args);
	}
}