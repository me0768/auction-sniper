package auctionsniper;

import lombok.ToString;

//@EqualsAndHashCode(of = {"itemId", "lastPrice", "lastBid"})
@ToString
public class SniperSnapshot {
  // 타입에서 어떤일을 하는지 정리하는 과정에서 pulbic final 필드를 사용
  // -> 값이 불변이라는 점이 드러남 + 클래스가 안정되지 않았을 때 getter를 유지해야하는 부담을 덜 수 있다.
  public final String itemId;
  public final int lastPrice;
  public final int lastBid;
  public final SniperState state;

  public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState sniperState) {
    this.itemId = itemId;
    this.lastPrice = lastPrice;
    this.lastBid = lastBid;
    this.state = sniperState;
  }

  public static SniperSnapshot joining(String itemId) {
    return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
  }

  public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
    return new SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING);
  }

  public SniperSnapshot winning(int newLastPrice) {
    return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING);
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
    result = prime * result + lastBid;
    result = prime * result + lastPrice;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SniperSnapshot other = (SniperSnapshot) obj;
    if (itemId == null) {
      if (other.itemId != null)
        return false;
    } else if (!itemId.equals(other.itemId))
      return false;
    if (lastBid != other.lastBid)
      return false;
    if (lastPrice != other.lastPrice)
      return false;
    return true;
  }
}
