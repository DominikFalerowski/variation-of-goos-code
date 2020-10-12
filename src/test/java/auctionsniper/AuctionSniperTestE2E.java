package auctionsniper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jxmpp.stringprep.XmppStringprepException;

import static auctionsniper.ApplicationRunnerE2E.SNIPER_XMPP_ID;

class AuctionSniperTestE2E {

    private FakeAuctionServerE2E auctionServer;
    private FakeAuctionServerE2E auctionServer2;
    private ApplicationRunnerE2E application;


    @BeforeEach
    void setUp() throws XmppStringprepException {
        application = new ApplicationRunnerE2E();
        auctionServer = new FakeAuctionServerE2E("item-54321");
        auctionServer2 = new FakeAuctionServerE2E("item-65432");
    }

    @AfterEach
    void tearDown() {
        auctionServer.stop();
        auctionServer2.stop();
        application.stop();
    }

    @Test
    void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auctionServer.startSellingItem();
        application.startBiddingIn(auctionServer);
        auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);
        auctionServer.announceClosed();
        application.showSniperHasLostAuction(auctionServer, 0, 0, 0);
    }

    @Test
    void sniperMakesAHigherBidButLoses() throws Exception {
        auctionServer.startSellingItem();
        application.startBiddingIn(auctionServer);
        auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);
        auctionServer.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auctionServer, 1000, 1098, 0);
        auctionServer.hasReceivedBid(1098, SNIPER_XMPP_ID);
        auctionServer.announceClosed();
        application.showSniperHasLostAuction(auctionServer, 1000, 1098, 0);
    }

    @Test
    void sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auctionServer.startSellingItem();
        application.startBiddingIn(auctionServer);
        auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);
        auctionServer.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auctionServer, 1000, 1098, 0); //last price, last bid
        auctionServer.hasReceivedBid(1098, SNIPER_XMPP_ID);
        auctionServer.reportPrice(1098, 97, SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auctionServer, 1098, 0); //winning bid
        auctionServer.announceClosed();
        application.showsSniperHasWonAuction(auctionServer, 1098, 0); //last price
    }

    @Test
    void sniperBidsForMultipleItems() throws Exception {
        auctionServer.startSellingItem();
        auctionServer2.startSellingItem();

        application.startBiddingIn(auctionServer, auctionServer2);
        auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);
        auctionServer2.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);

        auctionServer.reportPrice(1000, 98, "other bidder");
        auctionServer.hasReceivedBid(1098, SNIPER_XMPP_ID);

        auctionServer2.reportPrice(500, 21, "other bidder");
        auctionServer2.hasReceivedBid(521, SNIPER_XMPP_ID);

        auctionServer.reportPrice(1098, 97, SNIPER_XMPP_ID);
        auctionServer2.reportPrice(521, 22, SNIPER_XMPP_ID);

        application.hasShownSniperIsWinning(auctionServer, 1098, 0);
        application.hasShownSniperIsWinning(auctionServer2, 521, 1);

        auctionServer.announceClosed();
        auctionServer2.announceClosed();

        application.showsSniperHasWonAuction(auctionServer, 1098, 0);
        application.showsSniperHasWonAuction(auctionServer2, 521, 1);
    }

    @Test
    void sniperLosesAnAuctionWhenThePriceIsTooHigh() throws Exception {
        auctionServer.startSellingItem();
        application.startBiddingWithStopPrice(auctionServer, 1100, 0);
        auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);
        auctionServer.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auctionServer, 1000, 1098, 0);

        auctionServer.hasReceivedBid(1098, SNIPER_XMPP_ID);

        auctionServer.reportPrice(1197, 10, "third party");
        application.hasShownSniperIsLosing(auctionServer, 1197, 1098, 0);

        auctionServer.reportPrice(1207, 10, "fourth party");
        application.hasShownSniperIsLosing(auctionServer, 1207, 1098, 0);

        auctionServer.announceClosed();
        application.showSniperHasLostAuction(auctionServer, 1207, 1098, 0);
    }

    @Test
    void sniperReportsInvalidAuctionMessageAndStopsRespondingToEvents() throws Exception {
        String brokenMessage = "a broken message";
        auctionServer.startSellingItem();
        auctionServer2.startSellingItem();

        application.startBiddingIn(auctionServer, auctionServer2);
        auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);

        auctionServer.reportPrice(500, 20, "other bidder");
        auctionServer.hasReceivedBid(520, SNIPER_XMPP_ID);

        auctionServer.sendInvalidMessageContaining(brokenMessage);
        application.showsSniperHasFailed(auctionServer, 0);

        auctionServer.reportPrice(520, 21, "other bidder");
        waitForAnotherAuctionEvent();

        application.reportsInvalidMessage(auctionServer, brokenMessage);
        application.showsSniperHasFailed(auctionServer, 0);
    }

    private void waitForAnotherAuctionEvent() throws Exception {
        auctionServer2.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID);
        auctionServer2.reportPrice(600, 6, "other bidder");
        application.hasShownSniperIsBidding(auctionServer2, 600, 606, 1);
    }
}
