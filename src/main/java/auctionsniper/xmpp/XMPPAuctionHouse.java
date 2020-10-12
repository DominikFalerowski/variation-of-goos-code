package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.Item;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.parts.Resourcepart;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static auctionsniper.ConnectionConfig.configuration;
import static org.apache.commons.io.FilenameUtils.getFullPath;

public class XMPPAuctionHouse implements AuctionHouse {

    public static final String AUCTION_RESOURCE = "Auction";
    private static final String LOGGER_NAME = "auction-sniper";
    public static final String LOG_FILE_NAME = "auction-sniper.log";


    private final AbstractXMPPConnection connection;
    private final XMPPFailureReport failureReport;

    XMPPAuctionHouse(AbstractXMPPConnection connection) {
        this.connection = connection;
        this.failureReport = new LoggingXMPPFailureReporter(makeLogger());
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) {
        try {
            AbstractXMPPConnection connection = new XMPPTCPConnection(configuration(hostname, username, password));
            connection.connect();
            connection.login(username, password, Resourcepart.from(AUCTION_RESOURCE));
            return new XMPPAuctionHouse(connection);
        } catch (XMPPException | InterruptedException | IOException | SmackException e) {
            Thread.currentThread().interrupt();
            throw new XMMPAuctionException("Could not connect to auction ", e);
        }
    }

    @Override
    public Auction auctionFor(Item item) {
        return new XMPPAuction(connection, item, failureReport);
    }

    @Override
    public void disconnect() {
        connection.disconnect();
    }

    private Logger makeLogger() {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        logger.addHandler(simpleFileHandler());
        return logger;
    }

    private Handler simpleFileHandler() {
        try {
            FileHandler handler = new FileHandler(LOG_FILE_NAME);
            handler.setFormatter(new SimpleFormatter());
            return handler;
        } catch (Exception e) {
            throw new XMMPAuctionException("Could not create logger FileHandler " + getFullPath(LOG_FILE_NAME), e);
        }
    }
}
