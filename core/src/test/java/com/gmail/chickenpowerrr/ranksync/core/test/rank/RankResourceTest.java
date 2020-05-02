package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class RankResourceTest {

  @Test
  public void testSetup() {
    assertThat(new RankResource<TestPlatform>(true) {
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
    }.isCaseSensitive()).isTrue();

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
    }.isCaseSensitive()).isFalse();
  }
}
