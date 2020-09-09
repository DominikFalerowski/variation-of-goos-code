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
