package auctionsniper.unit.test;

import auctionsniper.Auction;
import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionSniperTest {

    @Mock
    SniperListener sniperListener;

    @Mock
    Auction auction;

    private AuctionSniper sniper;

    @BeforeEach
    void setUp() {
        sniper = new AuctionSniper(auction, sniperListener);
    }

    @Test
    void reportsLoseWhenAuctionCloses() {
        sniper.auctionClosed();

        verify(sniperListener, times(1)).sniperLost();
    }

    @Test
    void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        int price = 1001;
        int increment = 25;

        sniper.currentPrice(price, increment);

        verify(auction, times(1)).bid(price + increment);
        verify(sniperListener, atLeastOnce()).sniperBidding();
    }
}
