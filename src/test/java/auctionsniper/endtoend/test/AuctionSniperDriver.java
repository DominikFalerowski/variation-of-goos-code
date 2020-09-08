package auctionsniper.endtoend.test;

import auctionsniper.MainWindow;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.timing.Condition;

import static org.assertj.swing.timing.Pause.pause;

class AuctionSniperDriver {

    private final FrameFixture window;

    public AuctionSniperDriver(MainWindow ui) {
        FailOnThreadViolationRepaintManager.install();
        window = new FrameFixture(ui);
        window.show();
    }


    public void showsSniperStatus(String status) {
        JLabelFixture label = window.label(MainWindow.SNIPER_STATUS_NAME);
        pause(new Condition("Waiting to change text") {
            @Override
            public boolean test() {
                return label.text().equals(status);
            }
        }, 1000);

    }

    public void dispose() {
        window.cleanUp();
    }
}
