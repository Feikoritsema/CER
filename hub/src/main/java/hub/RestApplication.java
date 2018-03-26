package hub;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan(basePackageClasses = RestHandler.class)
public class RestApplication {
//	//public static void main(String[] args) {
//		SpringApplication.run(RestApplication.class, args);
//	}
}