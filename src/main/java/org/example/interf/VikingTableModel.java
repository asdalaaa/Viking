package org.example.interf;

import org.example.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {

    private final String[] columns = {"Id", "Name", "Age", "Height", "Hair", "Beard", "Equipment"};
    private final List<Viking> data = new ArrayList<>();

    public void addViking(Viking viking) {
        int index = findIndexById(viking.id());
        if (index >= 0) {
            data.set(index, viking);
            fireTableRowsUpdated(index, index);
        } else {
            data.add(viking);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
    }

    public void remove(int id) {
        int index = findIndexById(id);
        if (index >= 0) {
            data.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public void setRows(List<Viking> vikings) {
        data.clear();
        data.addAll(vikings);
        fireTableDataChanged();
    }

    private int findIndexById(Integer id) {
        if (id == null) return -1;
        for (int i = 0; i < data.size(); i++) {
            if (id.equals(data.get(i).id())) return i;
        }
        return -1;
    }

    @Override
    public int getRowCount() { return data.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int row, int column) {
        Viking v = data.get(row);
        return switch (column) {
            case 0 -> v.id();
            case 1 -> v.name();
            case 2 -> v.age();
            case 3 -> v.heightCm();
            case 4 -> v.hairColor();
            case 5 -> v.beardStyle();
            case 6 -> v.equipment() == null || v.equipment().isEmpty() ? "" :
                    v.equipment().stream()
                            .map(e -> e.name() + " [" + e.quality() + "]")
                            .collect(Collectors.joining(", "));
            default -> "";
        };
    }
}