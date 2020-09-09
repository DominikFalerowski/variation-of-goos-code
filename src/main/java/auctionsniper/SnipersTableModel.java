package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.MainWindow.STATUS_JOINING;

class SnipersTableModel extends AbstractTableModel {

    private static final SniperState STARTING_UP = new SniperState("", 0, 0);
    private String statusText = STATUS_JOINING;
    private SniperState sniperState = STARTING_UP;

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
                return sniperState.getItemId();
            case LAST_PRICE:
                return sniperState.getLastPrice();
            case LAST_BID:
                return sniperState.getLastBid();
            case SNIPER_STATUS:
                return statusText;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void sniperStatusChanged(SniperState sniperState, String statusText) {
        this.sniperState = sniperState;
        this.statusText = statusText;
        fireTableRowsUpdated(0, 0);
    }
}
