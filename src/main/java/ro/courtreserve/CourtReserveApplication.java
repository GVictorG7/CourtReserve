package ro.courtreserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CourtReserveApplication {
    public static void main(String... args) {
        SpringApplication.run(CourtReserveApplication.class, args);
    }
}
