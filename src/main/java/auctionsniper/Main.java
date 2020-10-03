package auctionsniper;


import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SwingThreadSniperListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static final String BID_COMMAND_FORMAT = "SQLVersion: 1.1; Command: BID; Price: %d";
    public static final String JOIN_COMMAND_FORMAT = "SQLVersion: 1.1; Command: JOIN";
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private final MainWindow ui;

    public Main(MainWindow ui) {
        this.ui = ui;
    }

    public static void main(MainWindow ui, String... args) {
        Main main = new Main(ui);
        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(auctionHouse);
        main.addUserRequestListenerFor(auctionHouse);
    }

    private void addUserRequestListenerFor(AuctionHouse auctionHouse) {
        ui.addUserRequestListener(itemId -> {
            ui.getSnipers().addSniper(SniperSnapshot.joining(itemId));
            Auction auction = auctionHouse.auctionFor(itemId);
            auction.addAuctionEventListener(new AuctionSniper(auction, new SwingThreadSniperListener(ui.getSnipers()), itemId));
            auction.join();
        });
    }

    private void disconnectWhenUICloses(AuctionHouse auctionHouse) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                auctionHouse.disconnect();
            }
        });
    }

}
