package auctionsniper.endtoend.test;


import auctionsniper.Main;
import auctionsniper.MainWindow;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import static auctionsniper.endtoend.test.FakeAuctionServer.XMPP_HOSTNAME;


class ApplicationRunner {

    private static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME;

    private AuctionSniperDriver driver;
    private MainWindow ui;

    public void startBiddingIn(FakeAuctionServer auction) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(ui, XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(ui);
        driver.showsSniperStatus(MainWindow.STATUS_JOINING);
    }

    public void showSniperHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STATUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(MainWindow.STATUS_BIDDING);

    }
}
