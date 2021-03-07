package auctionsniper;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class SniperState {
  // 타입에서 어떤일을 하는지 정리하는 과정에서 pulbic final 필드를 사용
  // -> 값이 불변이라는 점이 드러남 + 클래스가 안정되지 않았을 때 getter를 유지해야하는 부담을 덜 수 있다.
  public final String itemId;
  public final int lastPrice;
  public final int lastBid;

  public SniperState(String itemId, int lastPrice, int lastBid) {
    this.itemId = itemId;
    this.lastPrice = lastPrice;
    this.lastBid = lastBid;
  }
}
