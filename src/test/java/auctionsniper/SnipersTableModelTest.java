package auctionsniper;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import auctionsniper.ui.Column;
import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;

@ExtendWith(MockitoExtension.class)
public class SnipersTableModelTest {
  private TableModelListener listener = mock(TableModelListener.class);
  private final SnipersTableModel model = new SnipersTableModel();

  @BeforeEach
  public void attachModelListener(){
    model.addTableModelListener(listener);
  }

  @Test
  @DisplayName("올바른 개수의 칼럼이 렌더링되는가")
  public void hasEnoughColumns(){
    assertThat(model.getColumnCount(), equalTo(Column.values().length));
  }

  @Test
  @DisplayName("모델에 등록된 JTable의 내용이 변경되었는가")
  public void setsSniperValuesInColumns() {
    // 모델에 등록된 JTable이 변경됐음을 알리는지 검사
    // 책에는 aRowChangedEvent() 메서드로 Matcher를 만들어 비교함. 여기선 적절한 Mockito test로 변경
    verify(listener, times(1)).tableChanged(refEq(new TableModelEvent(model, 0)));

    // 변경을 일으키는 이벤트
    model.sniperStateChanged(new SniperSnapshot("item-id", 555, 666, SniperState.BIDDING));

    assertColumnEquals(Column.ITEM_IDENTIFIER, "item-id");
    assertColumnEquals(Column.LAST_PRICE, 555);
    assertColumnEquals(Column.LAST_BID, 666);
    assertColumnEquals(Column.SNIPER_STATE, MainWindow.STATUS_BIDDING);
  }

  private void assertColumnEquals(Column column, Object expected) {
    final int rowIndex = 0;
    final int columnIndex = column.ordinal();
    assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
  }

}
