package auctionsniper;

import javax.swing.*;

class SwingThreadSniperListener implements SniperListener {

    private final SnipersTableModel snipers;

    SwingThreadSniperListener(SnipersTableModel snipers) {
        this.snipers = snipers;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        SwingUtilities.invokeLater(() -> snipers.sniperStatusChanged(sniperSnapshot));
    }

}
