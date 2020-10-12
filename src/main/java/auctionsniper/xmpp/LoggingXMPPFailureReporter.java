package auctionsniper.xmpp;

import java.util.logging.Level;
import java.util.logging.Logger;

class LoggingXMPPFailureReporter implements XMPPFailureReport {

    private static final String MESSAGE_FORMAT = "<%s> Could not translate message \"%s\" because \"%s\"";
    private final Logger logger;

    LoggingXMPPFailureReporter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void cannotTranslateMessage(String auctionId, String failedMessage, Exception exception) {
        logger.log(Level.SEVERE, () -> String.format(MESSAGE_FORMAT, auctionId, failedMessage, exception.toString()));
    }
}
