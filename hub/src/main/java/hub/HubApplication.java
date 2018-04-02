package hub;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"hub", "message"})
public class HubApplication implements ApplicationRunner {

    public static void main(String... args) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(HubApplication.class);
        builder.headless(false).run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.containsOption("host")) {
            System.out.println("No host set, defaulting to localhost...");
        } else {
            System.out.println("Host set to: " + args.getOptionValues("host"));
        }
    }
}
