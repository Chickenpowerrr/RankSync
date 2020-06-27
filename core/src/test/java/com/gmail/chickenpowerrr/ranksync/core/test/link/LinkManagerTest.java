package com.gmail.chickenpowerrr.ranksync.core.test.link;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.gmail.chickenpowerrr.ranksync.core.link.LinkManager;
import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankLink;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import com.gmail.chickenpowerrr.ranksync.core.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class LinkManagerTest {

  private static final String NAME_FORMAT = "%name%";
  private static final int UPDATE_INTERVAL = 1;

  private RankLink rankLink;

  @Mock
  private Rank<?> rank1, rank2, rank3;

  @Mock
  private Platform<?> platform1, platform2;

  @BeforeEach
  public void setUp() {
    this.rankLink = new RankLink("1", NAME_FORMAT,
        Util.mapOf(this.platform1, Arrays.asList(this.rank1, this.rank2),
            this.platform2, Collections.singleton(this.rank3)));
  }

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
    User user = new User(linkManager, links);
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
    User user = new User(linkManager, links);
    links.add(new UserLink<>(getAccount(testPlatform, "test"), user));

    assertThat(linkManager.getCachedUsers()).isEmpty();

    linkManager.cacheUser(user);

    assertThat(linkManager.getCachedUsers()).hasSize(1);
    assertThat(linkManager.getCachedUsers()).contains(user);

    linkManager.unCacheUser(testPlatform, "test");
    assertThat(linkManager.getCachedUsers()).isEmpty();
  }

  @Test
  public void testRankLinks() {
    LinkManager linkManager = new LinkManager(true, UPDATE_INTERVAL, false,
        Collections.singleton(this.rankLink));
    assertThat(linkManager.getRankLinks()).hasSize(1);
    assertThat(linkManager.getRankLinks()).contains(this.rankLink);
  }

  @Test
  public void testForceUpdate() {
    LinkManager linkManager = new LinkManager(true, UPDATE_INTERVAL, false,
        Collections.singleton(this.rankLink));

    TestPlatform testPlatform = new TestPlatform();
    List<UserLink<?>> links = new ArrayList<>();
    User user = spy(new User(linkManager, links));
    links.add(new UserLink<>(getAccount(testPlatform, "test"), user));

    linkManager.cacheUser(user);
    linkManager.forceSync();

    verify(user, atLeastOnce()).update();
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

      @Override
      public void updateRanks(@NotNull List<Rank<T>> ranks) {

      }
    };
  }
}
