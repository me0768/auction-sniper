package auctionsniper;

import static com.objogate.wl.swing.driver.ComponentDriver.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import auctionsniper.AuctionEventListener.PriceSource;

@ExtendWith(MockitoExtension.class)
class AuctionSniperTest {

  private static final String ITEM_ID = "item-id";

  @Mock
  private SniperListener sniperListener;

  @Mock
  private Auction auction;

  @InjectMocks
  private AuctionSniper sniper;

  private SniperProcessState sniperState = SniperProcessState.IDLE;

  @Test
  void reports_lost_if_auction_closes_immediately() {
    sniper.auctionClosed();

    verify(sniperListener, times(1)).sniperLost();
  }

  @Test
  void reports_lost_if_auction_closes_when_bidding() {
    sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
    sniper.auctionClosed();

    ignoreStubs(auction);

    sniperListener.sniperBidding(any(SniperState.class)); // 여기서는 상태 내용에 관심 없고 다른 테스트의 변화에 따른 수정이므로 이렇게 처리.
    sniperState = SniperProcessState.BIDDING;
    verify(sniperListener, times(1)).sniperLost();

    //allowing(sniperListener).sniperBidding();
    //then(sniperState.is("bidding"));
    //verify(sniperListener, times(1)).sniperLost();
    //when(sniperState.is("bidding"));
  }

  @Test
  void bids_higher_and_reports_bidding_when_new_price_arrives() {
    final int price = 1001;
    final int increment = 25;
    final int bid = price + increment;

    sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

    verify(auction, times(1)).bid(bid);
    verify(sniperListener, atLeastOnce()).sniperBidding(new SniperState(ITEM_ID, price, bid));
  }
  
  @Test
  void reports_is_winning_when_current_price_comes_from_sniper() {
    sniper.currentPrice(123, 45, PriceSource.FromSniper);

    verify(sniperListener, atLeastOnce()).sniperWinning();
  }

  @Test
  void reports_won_if_auction_closes_when_winning() {
    sniper.currentPrice(123, 45, PriceSource.FromSniper);
    sniper.auctionClosed();

    ignoreStubs(auction);

    sniperListener.sniperWinning();
    sniperState = SniperProcessState.WINNING;
    verify(sniperListener, times(1)).sniperWon();
  }

  public enum SniperProcessState {
    IDLE, BIDDING, WINNING;
  }
}
