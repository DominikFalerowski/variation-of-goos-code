package auctionsniper.unit.test;

import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuctionSniperTest {

    @Mock
    SniperListener sniperListener;

    private AuctionSniper sniper;

    @BeforeEach
    void setUp() {
        sniper = new AuctionSniper(sniperListener);
    }

    @Test
    void reportsLoseWhenAuctionCloses() {
        sniper.auctionClosed();

        verify(sniperListener, times(1)).sniperLost();
    }
}
