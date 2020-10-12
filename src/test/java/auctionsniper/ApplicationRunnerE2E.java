package auctionsniper;


import static auctionsniper.FakeAuctionServerE2E.AUCTION_RESOURCE;
import static auctionsniper.FakeAuctionServerE2E.XMPP_HOSTNAME;
import static auctionsniper.SniperState.JOINING;
import static auctionsniper.ui.SnipersTableModel.textFor;


public class ApplicationRunnerE2E {

    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/" + AUCTION_RESOURCE;

    private AuctionSniperDriverE2E driver;

    protected static String[] arguments(FakeAuctionServerE2E... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }

        return arguments;
    }

    public void startBiddingIn(FakeAuctionServerE2E... auctions) {
        startSniper(auctions);
        for (int i = 0; i < auctions.length; i++) {
            FakeAuctionServerE2E auction = auctions[i];
            openBiddingFor(auction, Integer.MAX_VALUE, i);
        }
    }

    public void startBiddingWithStopPrice(FakeAuctionServerE2E auction, int stopPrice, int rowIndex) {
        startSniper();
        openBiddingFor(auction, stopPrice, rowIndex);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
            driver = null;
        }
    }

    public void hasShownSniperIsBidding(FakeAuctionServerE2E auction, int lastPrice, int lastBid, int rowIndex) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.BIDDING), rowIndex);
    }

    public void hasShownSniperIsWinning(FakeAuctionServerE2E auction, int winningBid, int rowIndex) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, textFor(SniperState.WINNING), rowIndex);
    }

    public void hasShownSniperIsLosing(FakeAuctionServerE2E auction, int winningBid, int lastBid, int rowIndex) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, lastBid, textFor(SniperState.LOSING), rowIndex);
    }

    public void showsSniperHasWonAuction(FakeAuctionServerE2E auction, int lastPrice, int rowIndex) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, textFor(SniperState.WON), rowIndex);
    }

    public void showSniperHasLostAuction(FakeAuctionServerE2E auction, int lastPrice, int lastBid, int rowIndex) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.LOST), rowIndex);
    }

    public void showsSniperHasFailed(FakeAuctionServerE2E auction, int rowIndex) {
        driver.showsSniperStatus(auction.getItemId(), 0, 0, textFor(SniperState.FAILED), rowIndex);
    }

    public void reportsInvalidMessage(FakeAuctionServerE2E auction, String brokenMessage) {

    }

    private void startSniper(FakeAuctionServerE2E... auctions) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(arguments(auctions));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriverE2E();
//        driver.hasColumnTitles();
    }

    private void openBiddingFor(FakeAuctionServerE2E auction, int stopPrice, int rowIndex) {
        driver.startBiddingFor(auction.getItemId(), stopPrice);
        driver.showsSniperStatus(auction.getItemId(), 0, 0, textFor(JOINING), rowIndex);
    }
}
