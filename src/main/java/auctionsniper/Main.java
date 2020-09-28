package auctionsniper;


import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SwingThreadSniperListener;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.parts.Resourcepart;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static auctionsniper.ConnectionConfig.configuration;

public class Main {

    public static final String BID_COMMAND_FORMAT = "SQLVersion: 1.1; Command: BID; Price: %d";
    public static final String JOIN_COMMAND_FORMAT = "SQLVersion: 1.1; Command: JOIN";
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private final MainWindow ui;

    public static final String AUCTION_RESOURCE = "Auction";

    public Main(MainWindow ui) {
        this.ui = ui;
    }

    public static void main(MainWindow ui, String... args) throws InterruptedException, IOException, SmackException, XMPPException {
        Main main = new Main(ui);
        AbstractXMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);
        main.addUserRequestListenerFor(connection);
    }

    private void addUserRequestListenerFor(AbstractXMPPConnection connection) {
        ui.addUserRequestListener(itemId -> {
            ui.getSnipers().addSniper(SniperSnapshot.joining(itemId));
            Auction auction = new XMPPAuction(connection, itemId);
            auction.addAuctionEventListener(new AuctionSniper(auction, new SwingThreadSniperListener(ui.getSnipers()), itemId));
            auction.join();
        });
    }

    private void disconnectWhenUICloses(AbstractXMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private static AbstractXMPPConnection connection(String hostname, String username, String password) throws XMPPException, InterruptedException, IOException, SmackException {
        AbstractXMPPConnection connection = new XMPPTCPConnection(configuration(hostname, username, password));
        connection.connect();
        connection.login(username, password, Resourcepart.from(AUCTION_RESOURCE));

        return connection;
    }
}
