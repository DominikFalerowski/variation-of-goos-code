package auctionsniper;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

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
                .setSendPresence(true)
                .build();
    }

}
