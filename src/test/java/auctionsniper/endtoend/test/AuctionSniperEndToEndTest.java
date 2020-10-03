package auctionsniper.endtoend.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.jxmpp.stringprep.XmppStringprepException;

import static auctionsniper.endtoend.test.ApplicationRunner.SNIPER_XMPP_ID;

class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private final FakeAuctionServer auctionServer2 = new FakeAuctionServer("item-65432");
    private final ApplicationRunner application = new ApplicationRunner();


    AuctionSniperEndToEndTest() throws XmppStringprepException {
    }

    @AfterEach
    void tearDown() {
        auctionServer.stop();
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
}
