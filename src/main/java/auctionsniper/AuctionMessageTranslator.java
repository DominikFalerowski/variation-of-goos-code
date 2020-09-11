package auctionsniper;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class AuctionMessageTranslator implements IncomingChatMessageListener {

    private final AuctionEventListener listener;
    private final String sniperId;
    private final EntityBareJid auctionEntityId;


    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener, EntityBareJid auctionEntityId) {
        this.sniperId = sniperId;
        this.listener = listener;
        this.auctionEntityId = auctionEntityId;
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        if (handle(from)) {
            AuctionEvent event = AuctionEvent.from(message.getBody());
            String eventType = event.type();

            if ("CLOSE".equals(eventType)) {
                listener.auctionClosed();
            } else if ("PRICE".equals(eventType)) {
                listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
            }
        }
    }

    private boolean handle(EntityBareJid auctionEntityId) {
        return this.auctionEntityId.equals(auctionEntityId);
    }
}
