package auctionsniper.ui;

import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public static final String SNIPERS_TABLE_NAME = "sniper table";
    public static final String NEW_ITEM_ID_NAME = "New item id";
    public static final String JOIN_BUTTON_NAME = "Join Button";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "New item stop price";

    private UserRequestListener userRequestListener;

    public MainWindow(SniperPortfolio portfolio) {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(portfolio), makeControls());
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        JTable snipersTable = new JTable(model);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(e -> {
            if (userRequestListener != null) {
                userRequestListener.joinAuction(itemIdField.getText());
            }
        });
        controls.add(joinAuctionButton);

        return controls;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        this.userRequestListener = userRequestListener;
    }
}
