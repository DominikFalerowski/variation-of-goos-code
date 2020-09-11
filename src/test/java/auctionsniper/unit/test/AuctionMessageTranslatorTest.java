package auctionsniper.unit.test;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.AuctionMessageTranslator;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuctionMessageTranslatorTest {

    private static final String SNIPER_ID = "SNIPER ID";

    @Mock
    AuctionEventListener listener;

    private static final Chat UNUSED_CHAT = null;
    private EntityBareJid entityBareJid;
    private AuctionMessageTranslator translator;

    @BeforeEach
    void setUp() throws XmppStringprepException {
        entityBareJid = JidCreate.entityBareFrom("any.any@any.pl");
        translator = new AuctionMessageTranslator(SNIPER_ID, listener, entityBareJid);
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

}
