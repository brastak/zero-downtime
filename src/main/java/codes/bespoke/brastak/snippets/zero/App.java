package codes.bespoke.brastak.snippets.zero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication(scanBasePackages = {"codes.bespoke.brastak.snippets.zero", "org.togglz.spring.boot.actuate"})
@EnableJdbcRepositories
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}