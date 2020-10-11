package auctionsniper;

class SniperLauncher implements UserRequestListener {

    private final AuctionHouse auctionHouse;
    private final SniperCollector sniperCollector;

    SniperLauncher(AuctionHouse auctionHouse, SniperCollector sniperCollector) {
        this.auctionHouse = auctionHouse;
        this.sniperCollector = sniperCollector;
    }

    @Override
    public void joinAuction(Item item) {
        Auction auction = auctionHouse.auctionFor(item);
        AuctionSniper sniper = new AuctionSniper(auction, item);
        auction.addAuctionEventListener(sniper);
        sniperCollector.addSniper(sniper);
        auction.join();
    }
}
