package org.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.example.controller.VikingListener;
import org.example.interf.VikingDesktopFrame;
import org.example.service.VikingLambdaService;
import org.example.service.VikingService;

import javax.swing.SwingUtilities;

@SpringBootApplication
public class VikingDemoApplication {

    public static void main(String[] args) {

        SpringApplication app =
                new SpringApplication(VikingDemoApplication.class);

        app.setHeadless(false);

        ConfigurableApplicationContext context = app.run(args);

        VikingService vikingService =
                context.getBean(VikingService.class);

        VikingLambdaService lambdaService =
                context.getBean(VikingLambdaService.class);

        VikingListener vikingListener =
                context.getBean(VikingListener.class);

        SwingUtilities.invokeLater(() -> {

            VikingDesktopFrame frame =
                    new VikingDesktopFrame(
                            vikingService,
                            lambdaService
                    );

            vikingListener.setGui(frame);

            frame.setVisible(true);
        });
    }
}