package auctionsniper;

import static auctionsniper.ui.SnipersTableModel.*;

import auctionsniper.ui.MainWindow;

public class ApplicationRunner {

  public static final String SNIPER_ID = "sniper";
  public static final String SNIPER_PASSWORD = "sniper";
  public static final String SNIPER_XMPP_ID = "sniper@local/Auction";

  private AuctionSniperDriver driver;
  private String itemId;

  public void startBiddingIn(final FakeAuctionServer auction) {
    Thread thread = new Thread("Test Application") {
      @Override
      public void run() {
        try {
          itemId = auction.getItemId();
          Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, itemId);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    thread.setDaemon(true);
    thread.start();
    driver = new AuctionSniperDriver(1_000);
    driver.hasTitle(MainWindow.APPLICATION_TITLE);
    driver.hasColumnTitles();
    driver.showsSniperStatus(JOINING.itemId, JOINING.lastPrice, JOINING.lastBid, textFor(SniperState.JOINING));
  }

  public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
    driver.showsSniperStatus(itemId, lastPrice, lastBid, textFor(SniperState.BIDDING));
  }

  public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
    driver.showsSniperStatus(itemId, lastPrice, lastBid, textFor(SniperState.LOST));
  }

  public void hasShownSniperIsWinning(int winningBid) {
    driver.showsSniperStatus(itemId, winningBid, winningBid, textFor(SniperState.WINNING));
  }

  public void showsSniperHasWonAuction(int lastPrice) {
    driver.showsSniperStatus(itemId, lastPrice, lastPrice, textFor(SniperState.WON));
  }

  public void stop() {
    if (driver != null) {
      driver.dispose();
    }
  }
}
