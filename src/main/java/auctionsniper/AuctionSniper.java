package auctionsniper;

public class AuctionSniper implements AuctionEventListener {

    private final Auction auction;
    private final SniperListener sniperListener;
    private SniperSnapshot sniperSnapshot;

    public AuctionSniper(Auction auction, SniperListener sniperListener, String itemId) {
        this.auction = auction;
        this.sniperListener = sniperListener;
        this.sniperSnapshot = SniperSnapshot.joining(itemId);
    }


    public void auctionClosed() {
        sniperSnapshot = sniperSnapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FROM_SNIPER:
                sniperSnapshot = sniperSnapshot.winning(price);
                break;
            case FROM_OTHER_BIDDER:
                int bid = price + increment;
                auction.bid(bid);
                sniperSnapshot = sniperSnapshot.bidding(price, bid);
                break;
        }
        notifyChange();
    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(sniperSnapshot);
    }
}
