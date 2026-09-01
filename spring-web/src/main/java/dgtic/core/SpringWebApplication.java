package dgtic.core;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SpringWebApplication {

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringWebApplication.class, args);
  }

}
