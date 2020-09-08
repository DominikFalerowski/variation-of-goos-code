package auctionsniper;

import java.util.HashMap;
import java.util.Map;

import static auctionsniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static auctionsniper.AuctionEventListener.PriceSource.FromSniper;

class AuctionEvent {

    private final Map<String, String> fields = new HashMap<>();


    public int currentPrice() {
        return getInt("CurrentPrice");
    }

    public String type() {
        return get("Event");
    }

    public int increment() {
        return getInt("Increment");
    }

    public AuctionEventListener.PriceSource isFrom(String sniperId) {
        return sniperId.equals(bidder()) ? FromSniper : FromOtherBidder;
    }

    static AuctionEvent from(String messageBody) {
        AuctionEvent event = new AuctionEvent();
        for (String field : fieldsIn(messageBody)) {
            event.addField(field);
        }

        return event;
    }

    static String[] fieldsIn(String messageBody) {
        return messageBody.split(";");
    }

    private String get(String fieldName) {
        return fields.get(fieldName);
    }

    private int getInt(String fieldName) {
        return Integer.parseInt(get(fieldName));
    }

    private void addField(String field) {
        String[] pair = field.split(":");
        fields.put(pair[0].trim(), pair[1].trim());
    }

    private String bidder() {
        return get("Bidder");
    }
}
