package auctionsniper;

import java.util.Objects;

public class SniperSnapshot {
    private final String itemId;
    private final int lastPrice;
    private final int lastBid;


    public SniperSnapshot(String itemId, int lastPrice, int lastBid) {
        this.itemId = itemId;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
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

    @Override
    public String toString() {
        return "SniperState{" +
                "itemId='" + itemId + '\'' +
                ", lastPrice=" + lastPrice +
                ", lastBid=" + lastBid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SniperSnapshot that = (SniperSnapshot) o;
        return lastPrice == that.lastPrice &&
                lastBid == that.lastBid &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, lastPrice, lastBid);
    }
}
