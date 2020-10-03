package auctionsniper;

class SniperLauncher implements UserRequestListener {

    private final AuctionHouse auctionHouse;
    private final SniperCollector sniperCollector;

    SniperLauncher(AuctionHouse auctionHouse, SniperCollector sniperCollector) {
        this.auctionHouse = auctionHouse;
        this.sniperCollector = sniperCollector;
    }

    @Override
    public void joinAuction(String itemId) {
        Auction auction = auctionHouse.auctionFor(itemId);
        AuctionSniper sniper = new AuctionSniper(auction, itemId);
        auction.addAuctionEventListener(sniper);
        sniperCollector.addSniper(sniper);
        auction.join();
    }
}
