package auctionsniper.unit.test;

import auctionsniper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static auctionsniper.AuctionEventListener.PriceSource.FROM_OTHER_BIDDER;
import static auctionsniper.AuctionEventListener.PriceSource.FROM_SNIPER;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionSniperTest {

    private static final String ITEM_ID = "any";

    @Mock
    SniperListener sniperListener;

    @Mock
    Auction auction;

    private AuctionSniper sniper;

    @BeforeEach
    void setUp() {
        sniper = new AuctionSniper(auction, sniperListener, ITEM_ID);
    }

    @Test
    void reportsLoseIfAuctionClosesImmediately() {
        sniper.auctionClosed();

        verify(sniperListener, times(1)).sniperStateChanged(new SniperSnapshot(ITEM_ID, 0, 0, SniperState.LOST));
    }

    @Test
    void reportLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, FROM_OTHER_BIDDER);
        sniper.auctionClosed();

        verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 168, SniperState.LOST));
    }

    @Test
    void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 45, FROM_SNIPER);
        sniper.auctionClosed();

        verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 0, SniperState.WINNING));
    }

    @Test
    void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        int price = 1001;
        int increment = 25;
        int bid = price + increment;

        sniper.currentPrice(price, increment, FROM_OTHER_BIDDER);

        verify(auction, times(1)).bid(bid);
        verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 1001, 1026, SniperState.BIDDING));
    }

    @Test
    void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 12, FROM_OTHER_BIDDER);
        sniper.currentPrice(135, 12, FROM_SNIPER);

        verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING));
    }
}
