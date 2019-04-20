package com.gmail.chickenpowerrr.ranksync.api.bot;

import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * This class is one of the easiest possible implementations of the LinkInfo interface
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BasicLinkInfo implements LinkInfo {

  private final String name;
  private final String linkExplanation;
}
