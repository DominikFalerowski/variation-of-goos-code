package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.MainWindow.STATUS_JOINING;

class SnipersTableModel extends AbstractTableModel {

    private static final SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.BIDDING);
    private String statusText = STATUS_JOINING;
    private SniperSnapshot sniperSnapshot = STARTING_UP;

    public void setStatusText(String statusText) {
        this.statusText = statusText;
        fireTableRowsUpdated(0, 0);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperSnapshot.getItemId();
            case LAST_PRICE:
                return sniperSnapshot.getLastPrice();
            case LAST_BID:
                return sniperSnapshot.getLastBid();
            case SNIPER_STATUS:
                return statusText;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void sniperStatusChanged(SniperSnapshot sniperSnapshot, String statusText) {
        this.sniperSnapshot = sniperSnapshot;
        this.statusText = statusText;
        fireTableRowsUpdated(0, 0);
    }
}
