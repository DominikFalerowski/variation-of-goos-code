package auctionsniper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.event.TableModelListener;

import java.util.Arrays;

import static auctionsniper.SnipersTableModel.textFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
        SniperSnapshot joining = SniperSnapshot.joining("item id");
        SniperSnapshot bidding = joining.bidding(555, 666);

        model.addSniper(joining);
        model.sniperStateChanged(bidding);

//        verify(listener, times(1)).tableChanged(eq(new TableModelEvent(model, 0, 0, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT)));
        assertRowMatchesSnapshot(0, bidding);
    }

    @Test
    void setsUpColumnHeadings() {
        Arrays.stream(Column.values()).forEach(column -> assertThat(column.getName()).isEqualTo(model.getColumnName(column.ordinal())));
    }

    @Test
    void notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");

        model.addSniper(joining);

//        verify(listener, times(1)).tableChanged(refEq(new TableModelEvent(model, 0, 0, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT)));
        assertThat(model.getRowCount()).isEqualTo(1);
        assertRowMatchesSnapshot(0, joining);
    }

    @Test
    void holdsSnipersInAdditionOrder() {
        model.addSniper(SniperSnapshot.joining("item 0"));
        model.addSniper(SniperSnapshot.joining("item 1"));

        assertThat(cellValue(0, Column.ITEM_IDENTIFIER)).isEqualTo("item 0");
        assertThat(cellValue(1, Column.ITEM_IDENTIFIER)).isEqualTo("item 1");
    }

    @Test
    void throwIAExceptionIfNoExistingSniperForAnUpdate() {
        assertThatIllegalArgumentException().isThrownBy(() -> model.sniperStateChanged(new SniperSnapshot("item 1", 124, 123, SniperState.WINNING)));
    }

    private void assertRowMatchesSnapshot(int row, SniperSnapshot sniperSnapshot) {
        assertThat(sniperSnapshot.getItemId()).isEqualTo(cellValue(row, Column.ITEM_IDENTIFIER));
        assertThat(sniperSnapshot.getLastPrice()).isEqualTo(cellValue(row, Column.LAST_PRICE));
        assertThat(sniperSnapshot.getLastBid()).isEqualTo(cellValue(row, Column.LAST_BID));
        assertThat(textFor(sniperSnapshot.getSniperState())).isEqualTo(cellValue(row, Column.SNIPER_STATUS));
    }

    private Object cellValue(int rowIndex, Column column) {
        return model.getValueAt(rowIndex, column.ordinal());
    }
}