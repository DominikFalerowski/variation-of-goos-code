package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

public enum Column {
    ITEM_IDENTIFIER("Item") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getItemId();
        }
    },
    LAST_PRICE("Last Price") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getLastPrice();
        }
    },
    LAST_BID("Last Bid") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getLastBid();
        }
    },
    SNIPER_STATUS("State") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return SnipersTableModel.textFor(sniperSnapshot.getSniperState());
        }
    };

    private final String name;

    Column(String name) {
        this.name = name;
    }

    public static Column at(int offset) {
        return values()[offset];
    }

    public abstract Object valueIn(SniperSnapshot sniperSnapshot);

    public String getName() {
        return name;
    }
}
