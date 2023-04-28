package net.morher.house.wled.strip;

import lombok.RequiredArgsConstructor;
import net.morher.house.wled.WledNode;
import net.morher.house.wled.style.LedStripState;

@RequiredArgsConstructor
public class WledSegment {
  private final WledNode node;
  private final int segmentId;
  private final Double adjustBrightness;

  public void updateState(LedStripState state) {
    LedStripState adjustedState = new LedStripState();
    adjustedState.apply(state, false);

    if (adjustBrightness != null && adjustedState.getBrightness() != null) {
      adjustedState.setBrightness((int) (adjustedState.getBrightness() * adjustBrightness));
    }

    node.updateSegment(segmentId, adjustedState);
  }
}
