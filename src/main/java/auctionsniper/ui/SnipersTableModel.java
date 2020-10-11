package auctionsniper.ui;

import auctionsniper.*;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, PortfolioListener {

    private static final String[] STATUS_TEXT = {"Joining", "Bidding", "Winning", "Losing","Lost", "Won", "Failed"};
    private final List<SniperSnapshot> snapshots = new ArrayList<>();

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

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


    @Override
    public void sniperAdded(AuctionSniper sniper) {
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));
    }

    private void addSniperSnapshot(SniperSnapshot snapshot) {
        snapshots.add(snapshot);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }
}
