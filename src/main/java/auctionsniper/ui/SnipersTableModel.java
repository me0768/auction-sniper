package auctionsniper.ui;

import static auctionsniper.ui.MainWindow.*;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

public class SnipersTableModel extends AbstractTableModel {
  private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
  private final static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING };
  private String statusText = STATUS_JOINING;
  private SniperSnapshot sniperSnapshot = STARTING_UP;


  public int getColumnCount() {
    return Column.values().length;
  }

  public int getRowCount() {
    return 1;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (Column.at(columnIndex)) {
      case ITEM_IDENTIFIER:
        return sniperSnapshot.itemId;
      case LAST_PRICE:
        return sniperSnapshot.lastPrice;
      case LAST_BID:
        return sniperSnapshot.lastBid;
      case SNIPER_STATE:
        return statusText;
      default:
        throw new IllegalArgumentException("No column at " + columnIndex);
    }
  }

  public void setStatusText(String newStatusText) {
    statusText = newStatusText;
    fireTableRowsUpdated(0, 0);
  }

  public void sniperStatusChanged(SniperSnapshot newSniperSnapshot) {
    sniperSnapshot = newSniperSnapshot;
    statusText = STATUS_TEXT[newSniperSnapshot.state.ordinal()];
    fireTableRowsUpdated(0, 0);
  }
}
