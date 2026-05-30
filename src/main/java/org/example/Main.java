package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setHeadless(false);

        ConfigurableApplicationContext context = app.run(args);

        ApplicationLauncher launcher = context.getBean(ApplicationLauncher.class);
        launcher.launch();
    }
}