package auctionsniper;

import java.util.Objects;

public class SniperSnapshot {
    private final String itemId;
    private final int lastPrice;
    private final int lastBid;
    private final SniperState sniperState;

    public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState sniperState) {
        this.itemId = itemId;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
        this.sniperState = sniperState;
    }

    public static SniperSnapshot joining(String itemId) {
        return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
    }

    public SniperSnapshot winning(int newLastPrice) {
        return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING);
    }

    public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
        return new SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING);
    }

    public SniperSnapshot losing(int price) {
        return new SniperSnapshot(itemId, price, lastBid, SniperState.LOSING);
    }

    public SniperSnapshot closed() {
        return new SniperSnapshot(itemId, lastPrice, lastBid, sniperState.whenAuctionClosed());
    }

    public SniperSnapshot failed() {
        return new SniperSnapshot(itemId, 0, 0, SniperState.FAILED);
    }

    public String getItemId() {
        return itemId;
    }

    public int getLastPrice() {
        return lastPrice;
    }

    public int getLastBid() {
        return lastBid;
    }

    public SniperState getSniperState() {
        return sniperState;
    }

    public boolean isForSameItemAs(SniperSnapshot sniperSnapshot) {
        return itemId.equals(sniperSnapshot.getItemId());
    }

    @Override
    public String toString() {
        return "SniperSnapshot{" +
                "itemId='" + itemId + '\'' +
                ", lastPrice=" + lastPrice +
                ", lastBid=" + lastBid +
                ", sniperState=" + sniperState +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SniperSnapshot that = (SniperSnapshot) o;
        return lastPrice == that.lastPrice &&
                lastBid == that.lastBid &&
                Objects.equals(itemId, that.itemId) &&
                sniperState == that.sniperState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, lastPrice, lastBid, sniperState);
    }
}
