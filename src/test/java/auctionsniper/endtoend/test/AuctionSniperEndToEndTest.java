package auctionsniper.endtoend.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.jxmpp.stringprep.XmppStringprepException;

class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    AuctionSniperEndToEndTest() throws XmppStringprepException {
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
        auction.hasReceivedJoinRequestFromSniper();
        auction.announceClosed();
        application.showSniperHasLostAuction();
    }
}
