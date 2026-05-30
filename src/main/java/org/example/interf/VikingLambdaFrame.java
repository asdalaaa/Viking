package org.example.interf;

import org.example.model.Viking;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.List;
import java.util.stream.Collectors;

public class VikingLambdaFrame extends JFrame {

    private final JLabel titleLabel = new JLabel(" ");
    private final JTextArea textArea = new JTextArea();

    public VikingLambdaFrame() {
        setTitle("Results");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel bottom = new JPanel();
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    public void showViking(Viking viking) {
        if (viking == null) {
            titleLabel.setText("Not found");
            textArea.setText("No vikings match the condition.");
            return;
        }
        titleLabel.setText("Random tall viking (>180)");
        textArea.setText(formatViking(viking));
    }

    public void showVikings(List<Viking> vikings) {
        if (vikings == null || vikings.isEmpty()) {
            titleLabel.setText("Empty");
            textArea.setText("No vikings match the condition.");
            return;
        }
        titleLabel.setText("Found: " + vikings.size());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vikings.size(); i++) {
            if (i > 0) sb.append("\n---\n");
            sb.append(formatViking(vikings.get(i)));
        }
        textArea.setText(sb.toString());
        textArea.setCaretPosition(0);
    }

    public void showText(String header, String body) {
        titleLabel.setText(header);
        textArea.setText(body);
        textArea.setCaretPosition(0);
    }

    private String formatViking(Viking v) {
        String equipment = (v.equipment() == null || v.equipment().isEmpty())
                ? "-"
                : v.equipment().stream()
                .map(e -> e.name() + " [" + e.quality() + "]")
                .collect(Collectors.joining(", "));
        return String.format(
                "ID: %d\nName: %s\nAge: %d\nHeight: %d cm\nHair: %s\nBeard: %s\nEquipment: %s",
                v.id(), v.name(), v.age(), v.heightCm(), v.hairColor(), v.beardStyle(), equipment
        );
    }
}