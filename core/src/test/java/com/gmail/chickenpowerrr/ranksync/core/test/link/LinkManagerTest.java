package com.gmail.chickenpowerrr.ranksync.core.test.link;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.link.LinkManager;
import org.junit.jupiter.api.Test;

public class LinkManagerTest {

  private static final String NAME_FORMAT = "%name%";
  private static final int UPDATE_INTERVAL = 1;

  @Test
  public void testSetup() {
    LinkManager linkManager = new LinkManager(NAME_FORMAT, true, UPDATE_INTERVAL, false);
    assertThat(linkManager.shouldSendWarnings(), is(true));
    assertThat(linkManager.shouldSyncNames(), is(false));

    linkManager = new LinkManager(NAME_FORMAT, false, UPDATE_INTERVAL, true);
    assertThat(linkManager.shouldSendWarnings(), is(false));
    assertThat(linkManager.shouldSyncNames(), is(true));
  }
}
