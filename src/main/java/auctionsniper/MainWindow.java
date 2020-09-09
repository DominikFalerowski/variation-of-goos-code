package auctionsniper;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame {

    public static final String STATUS_JOINING = "JOINING";
    public static final String STATUS_LOST = "LOST";
    public static final String STATUS_BIDDING = "BIDDING";
    public static final String STATUS_WINNING = "WINNING";
    public static final String STATUS_WON = "WON";
    public static final String SNIPERS_TABLE_NAME = "sniper table";

    private final SnipersTableModel snipers = new SnipersTableModel();

    private static final String MAIN_WINDOW_NAME = "Auction Sniper Main";

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void showStatusText(String statusText) {
        snipers.setStatusText(statusText);
    }

    private void fillContentPane(JTable snipersTable) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void sniperStatusChanged(SniperSnapshot sniperSnapshot) {
        snipers.sniperStatusChanged(sniperSnapshot);
    }
}
