package auctionsniper;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.*;
import static java.lang.String.*;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static org.hamcrest.Matchers.*;

import javax.swing.table.JTableHeader;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import auctionsniper.ui.Column;

public class AuctionSniperDriver extends JFrameDriver {

  public AuctionSniperDriver(int timeoutMillis) {
    super(new GesturePerformer(),
        JFrameDriver.topLevelFrame(named(Main.MAIN_WINDOW_NAME), showingOnScreen()),
        new AWTEventQueueProber(timeoutMillis, 100));
  }

  public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
    JTableDriver table = new JTableDriver(this);
    table.hasRow(
        matching(withLabelText(itemId),
            withLabelText(valueOf(lastPrice)),
            withLabelText(valueOf(lastBid)),
            withLabelText(statusText)));
  }

  public void hasColumnTitles() {
    JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
    headers.hasHeaders(matching(withLabelText(Column.ITEM_IDENTIFIER.name), withLabelText(Column.LAST_PRICE.name),
        withLabelText(Column.LAST_BID.name), withLabelText(Column.SNIPER_STATE.name)));

  }
}
