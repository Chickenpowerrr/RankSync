package com.gmail.chickenpowerrr.ranksync.core.test.link;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.chickenpowerrr.ranksync.core.link.LinkManager;
import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class LinkManagerTest {

  private static final String NAME_FORMAT = "%name%";
  private static final int UPDATE_INTERVAL = 1;

  @Test
  public void testSetup() {
    LinkManager linkManager = new LinkManager(true, UPDATE_INTERVAL, false);
    assertThat(linkManager.shouldSendWarnings()).isTrue();
    assertThat(linkManager.shouldSyncNames()).isFalse();

    linkManager = new LinkManager(false, UPDATE_INTERVAL, true);
    assertThat(linkManager.shouldSendWarnings()).isFalse();
    assertThat(linkManager.shouldSyncNames()).isTrue();
  }

  @Test
  public void testSync() {
    LinkManager linkManager = new LinkManager(true, UPDATE_INTERVAL, false);
    assertThat(linkManager.getCachedUsers()).isEmpty();

    TestPlatform testPlatform = new TestPlatform();
    List<UserLink<?>> links = new ArrayList<>();
    User user = new User(links);
    links.add(new UserLink<>(getAccount(testPlatform, "hey"), user));

    assertThat(linkManager.getCachedUsers()).isEmpty();

    linkManager.cacheUser(user);

    assertThat(linkManager.getCachedUsers()).hasSize(1);
    assertThat(linkManager.getCachedUsers()).contains(user);
  }

  @Test
  public void testUnSync() {
    LinkManager linkManager = new LinkManager(true, UPDATE_INTERVAL, false);
    assertThat(linkManager.getCachedUsers()).isEmpty();

    TestPlatform testPlatform = new TestPlatform();
    List<UserLink<?>> links = new ArrayList<>();
    User user = new User(links);
    links.add(new UserLink<>(getAccount(testPlatform, "test"), user));

    assertThat(linkManager.getCachedUsers()).isEmpty();

    linkManager.cacheUser(user);

    assertThat(linkManager.getCachedUsers()).hasSize(1);
    assertThat(linkManager.getCachedUsers()).contains(user);

    linkManager.unCacheUser(testPlatform, "test");
    assertThat(linkManager.getCachedUsers()).isEmpty();
  }

  private <T extends Platform<T>> Account<T> getAccount(@NotNull Platform<T> platform,
      @NotNull String identifier) {
    return new Account<T>(identifier, platform, new ArrayList<>()) {
      @Override
      public @NotNull String getName() {
        return "name";
      }

      @Override
      public boolean updateName(@NotNull String name) {
        return false;
      }

      @Override
      public @NotNull CompletableFuture<String> formatName() {
        return new CompletableFuture<>();
      }
    };
  }
}
