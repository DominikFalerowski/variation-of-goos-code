package auctionsniper.ui;

import auctionsniper.UserRequestListener;
import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
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
class MainWindowTest {

    private final SnipersTableModel snipersTableModel = new SnipersTableModel();
    private MainWindow mainWindow;
    private FrameFixture window;

    @Mock
    private UserRequestListener userRequestListener;

    @BeforeEach
    void setUp() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            mainWindow = new MainWindow(snipersTableModel);
            window = new FrameFixture(mainWindow);
        });
        mainWindow.addUserRequestListener(userRequestListener);
    }

    @Test
    void makesUserRequestWhenJoinButtonClicked() {
        window.textBox(MainWindow.NEW_ITEM_ID_NAME).deleteText().enterText("an item id");
        window.button(MainWindow.JOIN_BUTTON_NAME).click();

        verify(userRequestListener, times(1)).joinAuction("an item id");
    }
}