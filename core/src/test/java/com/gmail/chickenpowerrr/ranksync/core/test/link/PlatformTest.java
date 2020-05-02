package com.gmail.chickenpowerrr.ranksync.core.test.link;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.jetbrains.annotations.NotNull;
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
  private static final String BASE_NAME_FORMAT = "%name%";
  private static final List<Rank<TestPlatform>> FIRST_RANKS = Collections.singletonList(mock(Rank.class));
  private static final List<Rank<TestPlatform>> SECOND_RANKS = Collections.singletonList(mock(Rank.class));
  private static final List<Rank<TestPlatform>> ACCOUNT_RANKS = Collections.singletonList(mock(Rank.class));

  private TestPlatform platform;

  @Mock
  private Account<TestPlatform> account;

  @BeforeEach
  public void setUp() {
    this.platform = new TestPlatform(NAME, BASE_NAME_FORMAT, true);
  }

  @Test
  public void testSetup() throws ExecutionException, InterruptedException {
    assertThat(this.platform.getName()).isEqualTo(NAME);
    assertThat(this.platform.getRanks().get()).isEmpty();
    assertThat(this.platform.canChangeName()).isTrue();
  }

  @Test
  public void testRankResourceSingle() throws ExecutionException, InterruptedException {
    RankResource<TestPlatform> rankResource = getResource(FIRST_RANKS);
    assertThat(this.platform.getRanks().get()).isEmpty();
    assertThat(this.platform.getRanks(this.account).get()).isEmpty();

    this.platform.addRankResource(rankResource);
    assertThat(this.platform.getRanks().get()).containsAll(FIRST_RANKS);
    assertThat(this.platform.getRanks(this.account).get()).containsAll(ACCOUNT_RANKS);
  }

  @Test
  public void testRankResourceMultiple() throws ExecutionException, InterruptedException {
    RankResource<TestPlatform> rankResource1 = getResource(FIRST_RANKS);
    RankResource<TestPlatform> rankResource2 = getResource(SECOND_RANKS);

    assertThat(this.platform.getRanks().get()).isEmpty();
    assertThat(this.platform.getRanks(this.account).get()).isEmpty();

    this.platform.addRankResource(rankResource1);
    assertThat(this.platform.getRanks().get()).containsAll(FIRST_RANKS);
    assertThat(this.platform.getRanks(this.account).get()).containsAll(ACCOUNT_RANKS);

    this.platform.addRankResource(rankResource2);
    assertThat(this.platform.getRanks().get()).containsAll(FIRST_RANKS);
    assertThat(this.platform.getRanks().get()).containsAll(SECOND_RANKS);
    assertThat(this.platform.getRanks().get()).hasSize(FIRST_RANKS.size() + SECOND_RANKS.size());
    assertThat(this.platform.getRanks(this.account).get()).containsAll(ACCOUNT_RANKS);
  }

  private RankResource<TestPlatform> getResource(@NotNull Collection<Rank<TestPlatform>> ranks) {
    RankResource<TestPlatform> rankResource = mock(RankResource.class);
    when(rankResource.getRanks()).thenAnswer(invocation -> {
      CompletableFuture<Collection<Rank<TestPlatform>>> completableFuture = new CompletableFuture<>();
      completableFuture.complete(ranks);
      return completableFuture;
    });
    when(rankResource.getRanks(this.account)).thenAnswer(invocation -> {
      CompletableFuture<Collection<Rank<TestPlatform>>> completableFuture = new CompletableFuture<>();
      completableFuture.complete(ACCOUNT_RANKS);
      return completableFuture;
    });
    return rankResource;
  }
}
