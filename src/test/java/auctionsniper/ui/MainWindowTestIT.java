package auctionsniper.ui;

import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MainWindowTestIT {

    private final SniperPortfolio sniperPortfolio = new SniperPortfolio();
    private MainWindow mainWindow;
    private FrameFixture window;

    @Mock
    private UserRequestListener userRequestListener;

    @BeforeEach
    void setUp() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            mainWindow = new MainWindow(sniperPortfolio);
            window = new FrameFixture(mainWindow);
        });
        mainWindow.addUserRequestListener(userRequestListener);
    }

    @Test
    void makesUserRequestWhenJoinButtonClicked() {
        window.textBox(MainWindow.NEW_ITEM_ID_NAME).deleteText().enterText("an item id");
        window.textBox(MainWindow.NEW_ITEM_STOP_PRICE_NAME).deleteText().enterText("789");
        window.button(MainWindow.JOIN_BUTTON_NAME).click();

        verify(userRequestListener, times(1)).joinAuction(new Item("an item id", 789));
    }
}