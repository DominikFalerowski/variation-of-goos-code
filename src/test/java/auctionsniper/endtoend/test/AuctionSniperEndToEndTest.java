package auctionsniper.endtoend.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jxmpp.stringprep.XmppStringprepException;

class AuctionSniperEndToEndTest {

    private FakeAuctionServer auction;
    private FakeAuctionServer auction2;
    private ApplicationRunner application;

    @BeforeEach
    void setUp() throws XmppStringprepException {
        auction = new FakeAuctionServer("item-54321");
        auction2 = new FakeAuctionServer("item-65432");
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
        application.showSniperHasLostAuction(auction, 0, 0);
    }

    @Test
    void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction, 1000, 1098);
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showSniperHasLostAuction(auction,1000, 1098);
    }

    @Test
    void sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction, 1000, 1098); //last price, last bid
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction,1098); //winning bid
        auction.announceClosed();
        application.showsSniperHasWonAuction(auction,1098); //last price
    }

    @Test
    void sniperBidsForMultipleItems() throws Exception {
        auction.startSellingItem();
        auction2.startSellingItem();

        application.startBiddingIn(auction, auction2);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction2.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction2.reportPrice(500, 21, "other bidder");
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        auction2.reportPrice(521, 22, ApplicationRunner.SNIPER_XMPP_ID);

        application.hasShownSniperIsWinning(auction, 1098);
        application.hasShownSniperIsWinning(auction2, 521);

        auction.announceClosed();
        auction2.announceClosed();

        application.showsSniperHasWonAuction(auction, 1098);
        application.showsSniperHasWonAuction(auction2, 521);
    }
}
