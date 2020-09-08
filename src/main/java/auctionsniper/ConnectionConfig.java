package auctionsniper;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import static auctionsniper.Main.AUCTION_RESOURCE;

public class ConnectionConfig {

    private ConnectionConfig() {
    }

    public static XMPPTCPConnectionConfiguration configuration(String hostname, String username, String password) throws XmppStringprepException {
        return XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setHost(hostname)
                .setPort(5222)
                .setXmppDomain("desktop-eut9frn.mshome.net")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource(Resourcepart.from(AUCTION_RESOURCE))
                .setSendPresence(true)
                .build();
    }

}
