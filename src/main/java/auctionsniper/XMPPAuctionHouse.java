package auctionsniper;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.parts.Resourcepart;

import java.io.IOException;

import static auctionsniper.ConnectionConfig.configuration;

class XMPPAuctionHouse implements AuctionHouse {

    public static final String AUCTION_RESOURCE = "Auction";

    private final AbstractXMPPConnection connection;

    XMPPAuctionHouse(AbstractXMPPConnection connection) {
        this.connection = connection;
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) {
        try {
            AbstractXMPPConnection connection = new XMPPTCPConnection(configuration(hostname, username, password));
            connection.connect();
            connection.login(username, password, Resourcepart.from(AUCTION_RESOURCE));
            return new XMPPAuctionHouse(connection);
        } catch (XMPPException | InterruptedException | IOException | SmackException e) {
            Thread.currentThread().interrupt();
            throw new XmppAuctionException("Could not connect to auction ", e);
        }
    }

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    @Override
    public void disconnect() {
        connection.disconnect();
    }
}
