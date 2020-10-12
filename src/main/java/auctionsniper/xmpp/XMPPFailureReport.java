package auctionsniper.xmpp;

public interface XMPPFailureReport {

    void cannotTranslateMessage(String auctionId, String failedMessage, Exception exception);
}
