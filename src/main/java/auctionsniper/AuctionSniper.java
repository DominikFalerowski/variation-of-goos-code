package auctionsniper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuctionSniper implements AuctionEventListener {

    private final List<SniperListener> listeners = new ArrayList<>();
    private final Auction auction;
    private final Item item;
    private SniperSnapshot sniperSnapshot;

    public AuctionSniper(Auction auction, Item item) {
        this.auction = auction;
        this.sniperSnapshot = SniperSnapshot.joining(item.getIdentifier());
        this.item = item;
    }

    public void auctionClosed() {
        sniperSnapshot = sniperSnapshot.closed();
        notifyChange();
        auction.removeAuctionEventListener();
    }

    public void addSniperListener(SniperListener listener) {
        listeners.add(listener);
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FROM_SNIPER:
                sniperSnapshot = sniperSnapshot.winning(price);
                break;
            case FROM_OTHER_BIDDER:
                int bid = price + increment;
                if (item.allowsBid(bid)) {
                    auction.bid(bid);
                    sniperSnapshot = sniperSnapshot.bidding(price, bid);
                } else {
                    sniperSnapshot = sniperSnapshot.losing(price);
                }
                break;
        }
        notifyChange();
    }

    @Override
    public void auctionFailed() {
        sniperSnapshot = sniperSnapshot.failed();
        notifyChange();
        auction.removeAuctionEventListener();
    }

    private void notifyChange() {
        listeners.forEach(listener -> listener.sniperStateChanged(sniperSnapshot));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionSniper that = (AuctionSniper) o;
        return Objects.equals(auction, that.auction) &&
                Objects.equals(sniperSnapshot, that.sniperSnapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auction, sniperSnapshot);
    }

    public SniperSnapshot getSnapshot() {
        return sniperSnapshot;
    }
}
