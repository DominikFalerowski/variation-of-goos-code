package auctionsniper;

import java.util.ArrayList;
import java.util.List;

public class SniperPortfolio implements SniperCollector {

    private final List<PortfolioListener> listeners = new ArrayList<>();
    private final List<AuctionSniper> snipers = new ArrayList<>();

    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        listeners.forEach(listener -> listener.sniperAdded(sniper));
    }

    public void addPortfolioListener(PortfolioListener listener) {
        listeners.add(listener);
    }
}
