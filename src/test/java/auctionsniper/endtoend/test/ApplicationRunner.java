package auctionsniper.endtoend.test;


import auctionsniper.Main;
import auctionsniper.MainWindow;
import auctionsniper.SniperState;
import auctionsniper.SnipersTableModel;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import static auctionsniper.endtoend.test.FakeAuctionServer.AUCTION_RESOURCE;
import static auctionsniper.endtoend.test.FakeAuctionServer.XMPP_HOSTNAME;


class ApplicationRunner {

    private static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/" + AUCTION_RESOURCE;

    private AuctionSniperDriver driver;
    private MainWindow ui;
    private String itemId;

    public void startBiddingIn(FakeAuctionServer auction) throws InvocationTargetException, InterruptedException {
        itemId = auction.getItemId();
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(new SnipersTableModel()));
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
        driver.hasColumnTitles();
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.textFor(SniperState.BIDDING));
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, SnipersTableModel.textFor(SniperState.WINNING));
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.textFor(SniperState.WON));
    }

    public void showSniperHasLostAuction(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.textFor(SniperState.LOST));
    }
}
