package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.MainWindow.STATUS_JOINING;

class SnipersTableModel extends AbstractTableModel {

    private String statusText = STATUS_JOINING;

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
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return statusText;
    }

    public void sniperStatusChanged(SniperState sniperState, String statusText) {

    }

    public enum Column {
        ITEM_IDENTIFIER,
        LAST_PRICE,
        LAST_BID,
        SNIPER_STATUS;

        public static Column at(int offset) { return values()[offset]; }
    }
}
