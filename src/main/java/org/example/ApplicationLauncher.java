package org.example;

import org.example.interf.VikingDesktopFrame;
import org.example.controller.VikingListener;
import org.example.service.VikingLambdaService;
import org.example.service.VikingService;
import org.springframework.stereotype.Component;

import javax.swing.SwingUtilities;

@Component
public class ApplicationLauncher {

    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingListener vikingListener;

    public ApplicationLauncher(VikingService vikingService,
                               VikingLambdaService lambdaService,
                               VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;
        this.vikingListener = vikingListener;
    }

    public void launch() {
        SwingUtilities.invokeLater(() -> {
            VikingDesktopFrame frame = new VikingDesktopFrame(vikingService, lambdaService);
            vikingListener.setGui(frame);
            frame.setVisible(true);
        });
    }
}