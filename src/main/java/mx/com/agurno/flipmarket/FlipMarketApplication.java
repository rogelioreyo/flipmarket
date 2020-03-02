package mx.com.agurno.flipmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"mx.com.agurno.flipmarket"})
public class FlipMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlipMarketApplication.class, args);
    }
}