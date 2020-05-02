package com.gmail.chickenpowerrr.ranksync.core.test.link;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.chickenpowerrr.ranksync.core.link.LinkManager;
import org.junit.jupiter.api.Test;

public class LinkManagerTest {

  private static final String NAME_FORMAT = "%name%";
  private static final int UPDATE_INTERVAL = 1;

  @Test
  public void testSetup() {
    LinkManager linkManager = new LinkManager(NAME_FORMAT, true, UPDATE_INTERVAL, false);
    assertThat(linkManager.shouldSendWarnings()).isTrue();
    assertThat(linkManager.shouldSyncNames()).isFalse();

    linkManager = new LinkManager(NAME_FORMAT, false, UPDATE_INTERVAL, true);
    assertThat(linkManager.shouldSendWarnings()).isFalse();
    assertThat(linkManager.shouldSyncNames()).isTrue();
  }
}
