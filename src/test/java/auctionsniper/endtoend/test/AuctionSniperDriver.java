package auctionsniper.endtoend.test;

import auctionsniper.Column;
import auctionsniper.MainWindow;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableCellFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.timing.Condition;

import static org.assertj.swing.data.TableCell.row;
import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.assertj.swing.timing.Pause.pause;

class AuctionSniperDriver {

    private final FrameFixture window;
    private final JTableFixture table;

    public AuctionSniperDriver(MainWindow ui) {
        FailOnThreadViolationRepaintManager.install();
        window = new FrameFixture(ui);
        window.show();
        table = window.table(MainWindow.SNIPERS_TABLE_NAME);
    }


    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
        JTableCellFixture itemIdCell = table.cell(row(0).column(Column.ITEM_IDENTIFIER.ordinal()));
        JTableCellFixture lastPriceCell = table.cell(row(0).column(Column.LAST_PRICE.ordinal()));
        JTableCellFixture lastBidCell = table.cell(row(0).column(Column.LAST_BID.ordinal()));
        JTableCellFixture statusTextCell = table.cell(row(0).column(Column.SNIPER_STATUS.ordinal()));

        pause(new Condition("Waiting for row to change") {
            @Override
            public boolean test() {
                return execute(() -> assertEqualsTableCells(itemIdCell, itemId) &&
                        assertEqualsTableCells(lastPriceCell, String.valueOf(lastPrice)) &&
                        assertEqualsTableCells(lastBidCell, String.valueOf(lastBid)) &&
                        assertEqualsTableCells(statusTextCell, statusText));
            }
        }, 5000);
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
}
