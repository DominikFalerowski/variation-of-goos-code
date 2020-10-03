package auctionsniper;


import auctionsniper.ui.MainWindow;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static final String BID_COMMAND_FORMAT = "SQLVersion: 1.1; Command: BID; Price: %d";
    public static final String JOIN_COMMAND_FORMAT = "SQLVersion: 1.1; Command: JOIN";
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private MainWindow ui;

    public Main() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> this.ui = new MainWindow(portfolio));
    }

    public static void main(String... args) throws InvocationTargetException, InterruptedException {
        Main main = new Main();
        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(auctionHouse);
        main.addUserRequestListenerFor(auctionHouse);
    }

    private void addUserRequestListenerFor(AuctionHouse auctionHouse) {
        ui.addUserRequestListener(new SniperLauncher(auctionHouse, portfolio));
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
