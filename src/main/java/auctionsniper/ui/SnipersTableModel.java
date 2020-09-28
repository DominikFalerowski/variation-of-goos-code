package auctionsniper.ui;

import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel {

    private static final String[] STATUS_TEXT = {"Joining", "Bidding", "Winning", "Lost", "Won"};
    private final List<SniperSnapshot> snapshots = new ArrayList<>();

    @Override
    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).getName();
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        for (int row = 0; row < snapshots.size(); row++) {
            if (sniperSnapshot.isForSameItemAs(snapshots.get(row))) {
                snapshots.set(row, sniperSnapshot);
                fireTableRowsUpdated(row, row);
                return;
            }
        }
        throw new IllegalArgumentException("Cannot find match for " + sniperSnapshot);
    }

    public void addSniper(SniperSnapshot sniperSnapshot) {
        snapshots.add(sniperSnapshot);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }
}
