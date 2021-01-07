package edu.tamu.modspineomatic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ModSpineOMaticApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModSpineOMaticApplication.class, args);
    }

}
