package com.gmail.chickenpowerrr.ranksync.core.test.link;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class PlatformTest {

  private static final String NAME = "TestPlatform";
  private static final int MAX_NAME_LENGTH = 10;
  private static final List<Rank> FIRST_RANKS = Collections.singletonList(mock(Rank.class));
  private static final List<Rank> SECOND_RANKS = Collections.singletonList(mock(Rank.class));
  private static final List<Rank> ACCOUNT_RANKS = Collections.singletonList(mock(Rank.class));

  private Platform platform;

  @Mock
  private Account account;

  @BeforeEach
  public void setUp() {
    this.platform = new Platform(NAME, MAX_NAME_LENGTH, true) {
      @Nullable
      @Override
      public String formatName(@NotNull Account account, @NotNull String format) {
        return null;
      }

      @Override
      public boolean isValidFormat(@NotNull String format) {
        return false;
      }

      @Override
      public boolean isValidName(@NotNull String name) {
        return false;
      }
    };
  }

  @Test
  public void testSetup() throws ExecutionException, InterruptedException {
    assertThat(this.platform.getName(), is(equalTo(NAME)));
    assertThat((Collection<?>) this.platform.getRanks().get(), hasSize(0));
    assertThat(this.platform.canChangeName(), is(true));
  }

  @Test
  public void testRankResourceSingle() throws ExecutionException, InterruptedException {
    RankResource rankResource = getResource(FIRST_RANKS);
    assertThat((Collection<?>) this.platform.getRanks().get(), hasSize(0));
    assertThat((Collection<?>) this.platform.getRanks(this.account).get(), hasSize(0));

    this.platform.addRankResource(rankResource);
    assertThat((Collection<Rank>) this.platform.getRanks().get(),
        containsInAnyOrder(FIRST_RANKS.toArray()));
    assertThat((Collection<Rank>) this.platform.getRanks(this.account).get(),
        containsInAnyOrder(ACCOUNT_RANKS.toArray()));
  }

  @Test
  public void testRankResourceMultiple() throws ExecutionException, InterruptedException {
    RankResource rankResource1 = getResource(FIRST_RANKS);
    RankResource rankResource2 = getResource(SECOND_RANKS);
    assertThat((Collection<?>) this.platform.getRanks().get(), hasSize(0));
    assertThat((Collection<?>) this.platform.getRanks(this.account).get(), hasSize(0));

    this.platform.addRankResource(rankResource1);
    assertThat((Collection<Rank>) this.platform.getRanks().get(),
        containsInAnyOrder(FIRST_RANKS.toArray()));
    assertThat((Collection<Rank>) this.platform.getRanks(this.account).get(),
        containsInAnyOrder(ACCOUNT_RANKS.toArray()));

    this.platform.addRankResource(rankResource2);
    assertThat((Collection<Rank>) this.platform.getRanks().get(),
        hasItems(FIRST_RANKS.toArray(new Rank[0])));
    assertThat((Collection<Rank>) this.platform.getRanks().get(),
        hasItems(SECOND_RANKS.toArray(new Rank[0])));
    assertThat((Collection<Rank>) this.platform.getRanks().get(),
        hasSize(FIRST_RANKS.size() + SECOND_RANKS.size()));
    assertThat((Collection<Rank>) this.platform.getRanks(this.account).get(),
        containsInAnyOrder(ACCOUNT_RANKS.toArray()));
  }

  private RankResource getResource(@NotNull Collection<Rank> ranks) {
    RankResource rankResource = mock(RankResource.class);
    when(rankResource.getRanks()).thenAnswer(invocation -> {
      CompletableFuture<Collection<Rank>> completableFuture = new CompletableFuture<>();
      completableFuture.complete(ranks);
      return completableFuture;
    });
    when(rankResource.getRanks(this.account)).thenAnswer(invocation -> {
      CompletableFuture<Collection<Rank>> completableFuture = new CompletableFuture<>();
      completableFuture.complete(ACCOUNT_RANKS);
      return completableFuture;
    });
    return rankResource;
  }
}
