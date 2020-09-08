package auctionsniper.endtoend.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jxmpp.stringprep.XmppStringprepException;

class AuctionSniperEndToEndTest {

    private FakeAuctionServer auction;
    private ApplicationRunner application;

    @BeforeEach
    void setUp() throws XmppStringprepException {
        auction = new FakeAuctionServer("item-54321");
        application = new ApplicationRunner();
    }

    @AfterEach
    void tearDown() {
        auction.stop();
        application.stop();
    }

    @Test
    void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showSniperHasLostAuction();
    }

    @Test
    void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding();
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showSniperHasLostAuction();
    }

    @Test
    void sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding();
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning();
        auction.announceClosed();
        application.showsSniperHasWonAuction();
    }
}
