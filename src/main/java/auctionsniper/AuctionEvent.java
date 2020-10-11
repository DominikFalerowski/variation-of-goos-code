package auctionsniper;

import java.util.HashMap;
import java.util.Map;

import static auctionsniper.AuctionEventListener.PriceSource.FROM_OTHER_BIDDER;
import static auctionsniper.AuctionEventListener.PriceSource.FROM_SNIPER;

class AuctionEvent {

    private final Map<String, String> fields = new HashMap<>();

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
        return sniperId.equals(bidder()) ? FROM_SNIPER : FROM_OTHER_BIDDER;
    }

    private String get(String name) {
        String value = fields.get(name);
        if (value == null) {
            throw new MissingValueException(name);
        }
        return value;
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
