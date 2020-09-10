package auctionsniper.endtoend.test;


import auctionsniper.Main;
import auctionsniper.MainWindow;
import auctionsniper.SniperState;
import auctionsniper.SnipersTableModel;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static auctionsniper.SniperState.JOINING;
import static auctionsniper.SnipersTableModel.textFor;
import static auctionsniper.endtoend.test.FakeAuctionServer.AUCTION_RESOURCE;
import static auctionsniper.endtoend.test.FakeAuctionServer.XMPP_HOSTNAME;


class ApplicationRunner {

    private static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/" + AUCTION_RESOURCE;

    private AuctionSniperDriver driver;
    private MainWindow ui;

    public void startBiddingIn(FakeAuctionServer ...auctions) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(new SnipersTableModel()));
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(ui, arguments(auctions));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(ui);
        driver.hasColumnTitles();
        Arrays.stream(auctions).forEach(auction -> driver.showsSniperStatus(auction.getItemId(), 0, 0, textFor(JOINING)));
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.BIDDING));
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, textFor(SniperState.WINNING));
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, textFor(SniperState.WON));
    }

    public void showSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.LOST));
    }

    protected static String[] arguments(FakeAuctionServer ... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }

        return arguments;
    }
}
