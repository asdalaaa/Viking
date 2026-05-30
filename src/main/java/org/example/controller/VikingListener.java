package org.example.controller;

import org.example.interf.VikingDesktopFrame;
import org.example.model.Viking;
import org.springframework.stereotype.Component;

import javax.swing.SwingUtilities;

@Component
public class VikingListener {

    private VikingDesktopFrame frame;

    public void setGui(VikingDesktopFrame frame) {
        this.frame = frame;
    }

    public void onVikingAdded(Viking v) {
        if (frame == null || v == null) return;
        SwingUtilities.invokeLater(() -> frame.addNewViking(v));
    }

    public void onVikingDeleted(int id) {
        if (frame == null) return;
        SwingUtilities.invokeLater(() -> frame.removeViking(id));
    }
}