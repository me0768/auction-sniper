package auctionsniper;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import auctionsniper.AuctionEventListener.PriceSource;

@ExtendWith(MockitoExtension.class)
class AuctionSniperTest {

  private static final String ITEM_ID = "item-id";

  private SniperListener sniperListener = mock(SniperListener.class);

  private Auction auction = mock(Auction.class);

  private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);

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

    // allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
    sniperListener.sniperStateChanged(refEq(new SniperSnapshot("", 0, 0, SniperState.BIDDING)));
    sniperState = SniperProcessState.BIDDING;
    verify(sniperListener, times(1)).sniperLost();

    //allowing(sniperListener).sniperBidding();
    //then(sniperState.is("bidding"));
    //verify(sniperListener, times(1)).sniperLost();
    //when(sniperState.is("bidding"));
  }
  private Matcher<SniperSnapshot> aSniperThatIs(SniperState state) {
    return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state), "sniper that is", "was") {
      protected SniperState featureValueOf(SniperSnapshot actual) {
        return actual.state;
      }
    };
  }

  @Test
  void bids_higher_and_reports_bidding_when_new_price_arrives() {
    final int price = 1001;
    final int increment = 25;
    final int bid = price + increment;

    sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

    verify(auction, times(1)).bid(bid);
    verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
  }
  
  @Test
  void reports_is_winning_when_current_price_comes_from_sniper() {
    // listener가 snapshot에 들어이쓴 값을 수신할 수 있게 sniperWinnign으로부터 리스너를 호출하는 것을 sniperStateChanged()로 변경.
    // 첫번째로 호출하면 스나이퍼가 입찰(Bidding)하고, 두번째로 호출하면 낙찰했다(Winning)고 알린다.

    sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
    sniper.currentPrice(135, 45, PriceSource.FromSniper);
    verify(sniperListener, times(1)).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 135, SniperState.BIDDING));
    verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING));
  }

  @Test
  void reports_won_if_auction_closes_when_winning() {
    sniper.currentPrice(123, 45, PriceSource.FromSniper);
    sniper.auctionClosed();

    ignoreStubs(auction);

    verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 0, SniperState.WINNING));
    verify(sniperListener, times(1)).sniperWon();
  }

  public enum SniperProcessState {
    IDLE, BIDDING, WINNING;
  }
}
