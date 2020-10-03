package auctionsniper.endtoend.test;

import auctionsniper.ui.Column;
import auctionsniper.ui.MainWindow;
import org.assertj.swing.core.BasicComponentFinder;
import org.assertj.swing.core.ComponentFinder;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableCellFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.timing.Condition;

import javax.swing.*;

import static auctionsniper.ui.MainWindow.MAIN_WINDOW_NAME;
import static org.assertj.swing.data.TableCell.row;
import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.assertj.swing.timing.Pause.pause;

public class AuctionSniperDriver {

    private final FrameFixture window;
    private final JTableFixture table;

    public AuctionSniperDriver() {
        FailOnThreadViolationRepaintManager.install();
        ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
        JFrame mainWindow = finder.findByName(MAIN_WINDOW_NAME, JFrame.class, true);
        window = new FrameFixture(mainWindow);
        window.show();
        table = window.table(MainWindow.SNIPERS_TABLE_NAME);
    }


    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText, int rowIndex) {
        JTableCellFixture itemIdCell = table.cell(row(rowIndex).column(Column.ITEM_IDENTIFIER.ordinal()));
        JTableCellFixture lastPriceCell = table.cell(row(rowIndex).column(Column.LAST_PRICE.ordinal()));
        JTableCellFixture lastBidCell = table.cell(row(rowIndex).column(Column.LAST_BID.ordinal()));
        JTableCellFixture statusTextCell = table.cell(row(rowIndex).column(Column.SNIPER_STATUS.ordinal()));

        pause(new Condition("Waiting for row to change") {
            @Override
            public boolean test() {
                return execute(() -> assertEqualsTableCells(itemIdCell, itemId) &&
                        assertEqualsTableCells(lastPriceCell, String.valueOf(lastPrice)) &&
                        assertEqualsTableCells(lastBidCell, String.valueOf(lastBid)) &&
                        assertEqualsTableCells(statusTextCell, statusText));
            }
        }, 1000);
    }

    public void dispose() {
        window.cleanUp();
    }

    public void hasColumnTitles() {
        table.requireColumnNamed("Item");
        table.requireColumnNamed("Last Price");
        table.requireColumnNamed("Last Bid");
        table.requireColumnNamed("State");
    }

    private boolean assertEqualsTableCells(JTableCellFixture tableCell, String valueToCompare) {
        return tableCell != null && tableCell.value().equals(valueToCompare);
    }

    public void startBiddingFor(String itemId) {
        window.textBox(MainWindow.NEW_ITEM_ID_NAME).deleteText().enterText(itemId);
        window.button(MainWindow.JOIN_BUTTON_NAME).click();
    }
}
