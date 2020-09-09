package auctionsniper.endtoend.test;

import auctionsniper.MainWindow;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;

class AuctionSniperDriver {

    private final FrameFixture window;

    public AuctionSniperDriver(MainWindow ui) {
        FailOnThreadViolationRepaintManager.install();
        window = new FrameFixture(ui);
        window.show();
    }


    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
        JTableFixture table = window.table(MainWindow.SNIPERS_TABLE_NAME);
        table.requireContents(new String[][] {{itemId, String.valueOf(lastPrice), String.valueOf(lastBid), statusText}});
    }

    public void dispose() {
        window.cleanUp();
    }
}
