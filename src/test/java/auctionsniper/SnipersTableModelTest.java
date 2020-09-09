package auctionsniper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SnipersTableModelTest {

    @Mock
    TableModelListener listener;

    private SnipersTableModel model;

    @BeforeEach
    void setUp() {
        model = new SnipersTableModel();
        model.addTableModelListener(listener);
    }

    @Test
    void hasEnoughColumns() {
        assertThat(model.getColumnCount()).isEqualTo(Column.values().length);
    }

    @Test
    void setsSniperValuesInColumns() {
        model.sniperStatusChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));

        verify(listener, times(1)).tableChanged(refEq(new TableModelEvent(model, 0)));
        assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(Column.LAST_PRICE, 555);
        assertColumnEquals(Column.LAST_BID, 666);
        assertColumnEquals(Column.SNIPER_STATUS, SnipersTableModel.textFor(SniperState.BIDDING));

    }

    private void assertColumnEquals(Column column, Object expected) {
        int rowIndex = 0;
        int columnIndex = column.ordinal();
        assertThat(model.getValueAt(rowIndex, columnIndex)).isEqualTo(expected);
    }
}