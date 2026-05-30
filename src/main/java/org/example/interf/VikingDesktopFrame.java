package org.example.interf;

import org.example.model.BeardStyle;
import org.example.model.HairColor;
import org.example.model.Viking;
import org.example.service.VikingLambdaService;
import org.example.service.VikingService;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.stream.Collectors;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final JLabel status = new JLabel("Ready");

    public VikingDesktopFrame(VikingService vikingService, VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(0, 1, 4, 4));

        JButton createRandomButton = new JButton("Create random viking");
        createRandomButton.addActionListener(event -> onCreateViking());

        JButton reloadButton = new JButton("Reload table");
        reloadButton.addActionListener(event -> onReloadTable());

        JButton massGenButton = new JButton("Generate N vikings");
        massGenButton.addActionListener(event -> {
            String input = JOptionPane.showInputDialog(this, "How many vikings?", "10");
            if (input == null) return;
            try {
                int count = Integer.parseInt(input.trim());
                if (count <= 0) throw new NumberFormatException();
                vikingService.createRandomVikings(count);
                onReloadTable();
                status.setText("Generated " + count + " vikings");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton randomTallButton = new JButton("Random tall (>180)");
        randomTallButton.addActionListener(event -> {
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showViking(lambdaService.getRandomTallViking());
            frame.setVisible(true);
        });

        JButton legendaryButton = new JButton("Legendary equipment");
        legendaryButton.addActionListener(event -> {
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showVikings(lambdaService.getLegendaryVikings());
            frame.setVisible(true);
        });

        JButton redBeardButton = new JButton("Red beards (by age)");
        redBeardButton.addActionListener(event -> {
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showVikings(lambdaService.getSortedRedBeardedVikings());
            frame.setVisible(true);
        });

        JButton statsButton = new JButton("Statistics");
        statsButton.addActionListener(event -> showStatistics());

        JButton idMaxButton = new JButton("Max ID from int[]");
        idMaxButton.addActionListener(event -> {
            int maxId = lambdaService.findMaxId();
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showText("Max ID", maxId == -1 ? "Table is empty." : "Max ID: " + maxId);
            frame.setVisible(true);
        });

        JButton idEvenButton = new JButton("Even IDs from int[]");
        idEvenButton.addActionListener(event -> {
            int[] even = lambdaService.findEvenIds();
            String body = even.length == 0 ? "No even IDs." : "Found: " + even.length + "\n\n" +
                    Arrays.stream(even).mapToObj(String::valueOf).collect(Collectors.joining(", "));
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showText("Even IDs", body);
            frame.setVisible(true);
        });

        actions.add(createRandomButton);
        actions.add(reloadButton);
        actions.add(massGenButton);
        actions.add(randomTallButton);
        actions.add(legendaryButton);
        actions.add(redBeardButton);
        actions.add(statsButton);
        actions.add(idMaxButton);
        actions.add(idEvenButton);
        actions.add(status);

        add(actions, BorderLayout.EAST);

        onReloadTable();
    }

    private void showStatistics() {
        long older40 = lambdaService.countOlderThan(40);
        long younger25 = lambdaService.countYoungerThan(25);
        long range30to50 = lambdaService.countInAgeRange(30, 50);
        long outside30to50 = lambdaService.countOutsideAgeRange(30, 50);
        long braidedBlond = lambdaService.countByBeardAndHair(BeardStyle.BRAIDED, HairColor.Blond);
        long withAxes = lambdaService.countWithAxes();

        String body = String.format(
                "Age > 40: %d\nAge < 25: %d\nAge 30-50: %d\nAge outside 30-50: %d\n\n" +
                        "Braided + Blond: %d\n\nAxes (1 or 2): %d",
                older40, younger25, range30to50, outside30to50, braidedBlond, withAxes
        );

        VikingLambdaFrame frame = new VikingLambdaFrame();
        frame.showText("Statistics", body);
        frame.setVisible(true);
    }

    private void onCreateViking() {
        Viking created = vikingService.createRandom();
        addNewViking(created);
        status.setText("Added id " + created.id());
    }

    public void addNewViking(Viking viking) {
        onReloadTable();
        status.setText("Shown id " + viking.id());
    }

    public void removeViking(int id) {
        tableModel.remove(id);
        status.setText("Removed id " + id);
    }

    public void onReloadTable() {
        tableModel.setRows(vikingService.loadAll());
        status.setText("Rows: " + tableModel.getRowCount());
    }
}