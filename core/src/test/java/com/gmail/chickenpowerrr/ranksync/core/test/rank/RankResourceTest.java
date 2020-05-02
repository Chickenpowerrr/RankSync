package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.hamcrest.MatcherAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class RankResourceTest {

  @Test
  public void testSetup() {
    MatcherAssert.assertThat(new RankResource<TestPlatform>(true) {
      @Override
      public @NotNull CompletableFuture<Collection<Rank<TestPlatform>>> getRanks(@NotNull Account<TestPlatform> account) {
        return null;
      }

      @Override
      public @NotNull CompletableFuture<Collection<Rank<TestPlatform>>> getRanks() {
        return null;
      }

      @Override
      public void applyRanks(@NotNull Account<TestPlatform> account,
          @NotNull Collection<Rank<TestPlatform>> ranks) {

      }
    }.isCaseSensitive(), is(true));

    assertThat(new RankResource<TestPlatform>(false) {
      @Override
      public @NotNull CompletableFuture<Collection<Rank<TestPlatform>>> getRanks(@NotNull Account<TestPlatform> account) {
        return null;
      }

      @Override
      public @NotNull CompletableFuture<Collection<Rank<TestPlatform>>> getRanks() {
        return null;
      }

      @Override
      public void applyRanks(@NotNull Account<TestPlatform> account,
          @NotNull Collection<Rank<TestPlatform>> ranks) {

      }
    }.isCaseSensitive(), is(false));
  }
}
