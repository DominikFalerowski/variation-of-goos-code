package auctionsniper;


import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static auctionsniper.ConnectionConfig.configuration;
import static java.lang.String.format;

public class Main implements SniperListener {

    public static final String BID_COMMAND_FORMAT = "SQLVersion: 1.1; Command: BID; Price: %d";
    public static final String JOIN_COMMAND_FORMAT = "SQLVersion: 1.1; Command: JOIN";
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;
    private final MainWindow ui;

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    public Main(MainWindow ui) {
        this.ui = ui;
    }

    public static void main(MainWindow ui, String... args) throws InterruptedException, IOException, SmackException, XMPPException {
        Main main = new Main(ui);
        main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]), args[ARG_ITEM_ID]);
    }

    @Override
    public void sniperLost() {
        SwingUtilities.invokeLater(() -> ui.showStatus(MainWindow.STATUS_LOST));
    }

    @Override
    public void sniperBidding() {
        SwingUtilities.invokeLater(() -> ui.showStatus(MainWindow.STATUS_BIDDING));
    }

    private void joinAuction(AbstractXMPPConnection connection, String itemId) throws XmppStringprepException {
        disconnectWhenUICloses(connection);
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        Chat chat = chatManager.chatWith(JidCreate.entityBareFrom(auctionId(itemId, connection)));
        Auction auction = new XMPPAuction(chat);
        chatManager.addIncomingListener(new AuctionMessageTranslator(new AuctionSniper(auction, this)));
        auction.join();
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

    private static String auctionId(String itemId, AbstractXMPPConnection connection) {
        return format(AUCTION_ID_FORMAT, itemId, connection.getUser().getDomain());
    }
}
