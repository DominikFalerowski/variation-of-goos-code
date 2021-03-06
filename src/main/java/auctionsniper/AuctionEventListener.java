package auctionsniper;

public interface AuctionEventListener {

    void auctionClosed();

    void currentPrice(int price, int increment, PriceSource priceSource);

    void auctionFailed();

    enum PriceSource {
        FROM_SNIPER, FROM_OTHER_BIDDER
    }
}
