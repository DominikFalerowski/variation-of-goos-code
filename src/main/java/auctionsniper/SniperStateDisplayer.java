package auctionsniper;

import javax.swing.*;

class SniperStateDisplayer implements SniperListener {

    private final MainWindow ui;

    SniperStateDisplayer(MainWindow ui) {
        this.ui = ui;
    }

    @Override
    public void sniperLost() {
        showStatus(MainWindow.STATUS_LOST);
    }

    @Override
    public void sniperBidding() {
        showStatus(MainWindow.STATUS_BIDDING);
    }

    @Override
    public void sniperWinning() {
        showStatus(MainWindow.STATUS_WINNING);
    }

    @Override
    public void sniperWon() {
        showStatus(MainWindow.STATUS_WON);
    }

    private void showStatus(String statusWinning) {
        SwingUtilities.invokeLater(() -> ui.showStatus(statusWinning));
    }
}
