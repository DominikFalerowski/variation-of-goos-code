package auctionsniper;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class AuctionMessageTranslator implements IncomingChatMessageListener {

    private final AuctionEventListener listener;


    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        AuctionEvent event = AuctionEvent.from(message.getBody());
        String eventType = event.type();

        if ("CLOSE".equals(eventType)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(eventType)) {
            listener.currentPrice(event.currentPrice(), event.increment());
        }
    }
}
