package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Item;
import auctionsniper.ApplicationRunnerE2E;
import auctionsniper.FakeAuctionServerE2E;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class XMPPAuctionHouseTest {

    private final FakeAuctionServerE2E auctionServer = new FakeAuctionServerE2E("item-54321");
    private XMPPAuctionHouse auctionHouse;

    XMPPAuctionHouseTest() throws XmppStringprepException {
    }

    @BeforeEach
    void setUp() throws InterruptedException, IOException, SmackException, XMPPException {
        auctionHouse = XMPPAuctionHouse.connect(FakeAuctionServerE2E.XMPP_HOSTNAME, ApplicationRunnerE2E.SNIPER_ID, ApplicationRunnerE2E.SNIPER_PASSWORD);
        auctionServer.startSellingItem();
    }

    @AfterEach
    void tearDown() {
        if (auctionHouse != null) {
            auctionHouse.disconnect();
        }
        auctionServer.stop();
    }

    @Test
    void receivesEventsFromAuctionServerAfterJoining() throws InterruptedException, SmackException.NotConnectedException {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(new Item(auctionServer.getItemId(), 567));
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();
        auctionServer.hasReceivedJoinRequestFromSniper(ApplicationRunnerE2E.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertThat(auctionWasClosed.await(4, TimeUnit.SECONDS)).as("should have been closed").isTrue();
    }

    private AuctionEventListener auctionClosedListener(CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {

            }

            @Override
            public void auctionFailed() {

            }
        };
    }
}