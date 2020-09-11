package auctionsniper;

import java.util.EventListener;

interface UserRequestListener extends EventListener {

    void joinAuction(String itemId);
}
