package auctionsniper.endtoend.test;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static auctionsniper.ConnectionConfig.configuration;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class FakeAuctionServer {

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";

    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final AbstractXMPPConnection connection;
    private final SingleMessageListener messageListener = new SingleMessageListener();
    private static Chat currentChat;

    public FakeAuctionServer(String itemId) throws XmppStringprepException {
        this.itemId = itemId;
        this.connection = new XMPPTCPConnection(configuration(XMPP_HOSTNAME, format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD));
    }

    public void startSellingItem() throws XMPPException, InterruptedException, IOException, SmackException {
        connection.connect();
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, Resourcepart.from(AUCTION_RESOURCE));
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(messageListener);
    }

    public String getItemId() {
        return itemId;
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws SmackException.NotConnectedException, InterruptedException {
        currentChat.send("Closing auction...");
    }

    public void stop() {
        connection.disconnect();
    }

    private static class SingleMessageListener implements IncomingChatMessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);


        void receivesAMessage() throws InterruptedException {
            assertThat(messages.poll(5, TimeUnit.SECONDS)).isNotNull();
        }

        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            messages.add(message);
            currentChat = chat;
        }
    }
}
