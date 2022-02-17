package pt.ulusofona.tfc.trabalho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class TrabalhoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrabalhoApplication.class, args);
    }
}
