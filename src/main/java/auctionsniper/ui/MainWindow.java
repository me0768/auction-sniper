package auctionsniper.ui;

import java.awt.*;

import javax.swing.*;

import auctionsniper.Main;
import auctionsniper.SniperSnapshot;

public class MainWindow extends JFrame {

  public static final String APPLICATION_TITLE = "Auction Sniper";
  public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
  public static final String SNIPERS_TABLE_NAME= "sniper status";
  private final SnipersTableModel snipers;

  public MainWindow(SnipersTableModel snipers) {
    super("Auction Sniper");
    this.snipers = snipers;
    setName(Main.MAIN_WINDOW_NAME);
    fillContentPane(makeSnipersTable());
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private void fillContentPane(JTable snipersTable) {
    final Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
  }

  private JTable makeSnipersTable() {
    final JTable snipersTable = new JTable(snipers);
    snipersTable.setName(SNIPERS_TABLE_NAME);
    return snipersTable;
  }

}
