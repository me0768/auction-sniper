package auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
  public final static SniperSnapshot JOINING = new SniperSnapshot("", 0, 0, SniperState.JOINING);
  private final static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };
  private SniperSnapshot sniperSnapshot = JOINING;


  public int getColumnCount() {
    return Column.values().length;
  }

  public int getRowCount() {
    return 1;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    return Column.at(columnIndex).valueIn(sniperSnapshot);
  }

  public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
    sniperSnapshot = newSniperSnapshot;
    fireTableRowsUpdated(0, 0);
  }

  public String getColumnName(int columnIndex) {
    return Column.at(columnIndex).name;
  }

  public static String textFor(SniperState state) {
    return STATUS_TEXT[state.ordinal()];
  }
}
