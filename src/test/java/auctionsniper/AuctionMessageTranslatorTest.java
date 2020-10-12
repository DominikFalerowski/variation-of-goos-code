package auctionsniper;

import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.xmpp.XMPPFailureReport;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuctionMessageTranslatorTest {

    private static final String SNIPER_ID = "SNIPER ID";
    private static final Chat UNUSED_CHAT = null;
    @Mock
    AuctionEventListener listener;
    @Mock
    XMPPFailureReport failureReport;
    private EntityBareJid entityBareJid;
    private AuctionMessageTranslator translator;

    @BeforeEach
    void setUp() throws XmppStringprepException {
        entityBareJid = JidCreate.entityBareFrom("any.any@any.pl");
        translator = new AuctionMessageTranslator(SNIPER_ID, listener, entityBareJid, failureReport);
    }

    @Test
    void notifiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message();
        message.setBody("SQLVersion: 1.1; Event: CLOSE;");

        translator.newIncomingMessage(entityBareJid, message, UNUSED_CHAT);

        verify(listener, times(1)).auctionClosed();
    }

    @Test
    void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        Message message = new Message();
        message.setBody("SQLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");

        translator.newIncomingMessage(entityBareJid, message, UNUSED_CHAT);

        verify(listener, times(1)).currentPrice(192, 7, PriceSource.FROM_OTHER_BIDDER);
    }

    @Test
    void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        Message message = new Message();
        message.setBody("SQLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: " + SNIPER_ID + ";");

        translator.newIncomingMessage(entityBareJid, message, UNUSED_CHAT);

        verify(listener, times(1)).currentPrice(192, 7, PriceSource.FROM_SNIPER);
    }

    @Test
    void notifiesAuctionFailedWhenBadMessageReceived() {
        Message message = new Message();
        message.setBody("a bad message");

        translator.newIncomingMessage(entityBareJid, message, UNUSED_CHAT);

        expectFailureWithMessage(message);
    }

    private void expectFailureWithMessage(Message message) {
        verify(listener, times(1)).auctionFailed();
        verify(failureReport, times(1)).cannotTranslateMessage(eq(SNIPER_ID), eq(message.getBody()), any(RuntimeException.class));
    }

    @Test
    void notifiesAuctionFailedWhenEventTypeMissing() {
        Message message = new Message();
        message.setBody("SQLVersion: 1.1; CurrentPrice: 234; Increment: 5; Bidder: " + SNIPER_ID + ";");

        translator.newIncomingMessage(entityBareJid, message, UNUSED_CHAT);

        verify(listener, times(1)).auctionFailed();
    }
}
