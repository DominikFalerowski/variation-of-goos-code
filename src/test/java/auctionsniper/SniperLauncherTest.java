package auctionsniper;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class SniperLauncherTest {
    private final AuctionHouse auctionHouse = mock(AuctionHouse.class);
    private final SniperCollector sniperCollector = mock(SniperCollector.class);
    private final Auction auction = mock(Auction.class);
    private final SniperLauncher sniperLauncher = new SniperLauncher(auctionHouse, sniperCollector);

    @Test
    void addsNewSniperToCollectorAndThenJoinsAuction() {
        Item item = new Item("item 123", 1234);
        when(auctionHouse.auctionFor(item)).thenReturn(auction);
        AuctionSniper auctionSniper = new AuctionSniper(auction, item);

        sniperLauncher.joinAuction(item);

        verify(auction, times(1)).addAuctionEventListener(auctionSniper);
        verify(sniperCollector, times(1)).addSniper(auctionSniper);
    }
}