package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionMessageTranslator;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import static auctionsniper.Main.BID_COMMAND_FORMAT;
import static auctionsniper.Main.JOIN_COMMAND_FORMAT;
import static java.lang.String.format;

public class XMPPAuction implements Auction {

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    private final Chat chat;
    private final ChatManager chatManager;
    private final AbstractXMPPConnection connection;
    private final EntityBareJid entityAuctionId;

    public XMPPAuction(AbstractXMPPConnection connection, String itemId) {
        this.connection = connection;
        chatManager = ChatManager.getInstanceFor(connection);
        entityAuctionId = entityId(auctionId(itemId, connection));
        chat = chatManager.chatWith(entityAuctionId);
    }

    @Override
    public void bid(int amount) {
        sendMessage(format(BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    @Override
    public void addAuctionEventListener(AuctionEventListener auctionEventListener) {
        chatManager.addIncomingListener(new AuctionMessageTranslator(connection.getUser().toString(), auctionEventListener, entityAuctionId));
    }

    private void sendMessage(String message) {
        if (chat != null) {
            try {
                chat.send(message);
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private EntityBareJid entityId(String auctionId) {
        try {
            return JidCreate.entityBareFrom(auctionId);
        } catch (XmppStringprepException e) {
            throw new XMPPParsingException(e);
        }
    }

    private String auctionId(String itemId, AbstractXMPPConnection connection) {
        return format(AUCTION_ID_FORMAT, itemId, connection.getUser().getDomain());
    }
}
