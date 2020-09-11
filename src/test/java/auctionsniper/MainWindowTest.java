package auctionsniper;

import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MainWindowTest {

    private final SnipersTableModel snipersTableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(snipersTableModel);
    private final FrameFixture window = new FrameFixture(mainWindow);

    @Mock
    private UserRequestListener userRequestListener;

    @BeforeEach
    void setUp() {
        mainWindow.addUserRequestListener(userRequestListener);
    }

    @Test
    void makesUserRequestWhenJoinButtonClicked() {
        window.textBox(MainWindow.NEW_ITEM_ID_NAME).deleteText().enterText("an item id");
        window.button(MainWindow.JOIN_BUTTON_NAME).click();

        verify(userRequestListener, times(1)).joinAuction("an item id");
    }
}