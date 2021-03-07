package auctionsniper;

import javax.swing.*;

import auctionsniper.ui.SnipersTableModel;

public class SwingThreadSniperListener implements SniperListener {
  private SnipersTableModel snipers;

  public SwingThreadSniperListener(SnipersTableModel snipers) {
    this.snipers = snipers;
  }

  public void sniperStateChanged(final SniperSnapshot snapshot) {
    SwingUtilities.invokeLater(() -> snipers.sniperStateChanged(snapshot));
  }
}
