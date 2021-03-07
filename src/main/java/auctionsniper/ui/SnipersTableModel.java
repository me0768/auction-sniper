package auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

public class SnipersTableModel extends AbstractTableModel {
  private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
  private final static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };
  private SniperSnapshot sniperSnapshot = STARTING_UP;


  public int getColumnCount() {
    return Column.values().length;
  }

  public int getRowCount() {
    return 1;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    return Column.at(columnIndex).valueIn(sniperSnapshot);
  }

  public void sniperStatusChanged(SniperSnapshot newSniperSnapshot) {
    sniperSnapshot = newSniperSnapshot;
    fireTableRowsUpdated(0, 0);
  }

  public static String textFor(SniperState state) {
    return STATUS_TEXT[state.ordinal()];
  }
}
