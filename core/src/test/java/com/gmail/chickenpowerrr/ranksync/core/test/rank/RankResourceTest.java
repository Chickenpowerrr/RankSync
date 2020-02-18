package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.hamcrest.MatcherAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class RankResourceTest {

  @Test
  public void testSetup() {
    MatcherAssert.assertThat(new RankResource<Platform>(true) {
      @Override
      public @NotNull CompletableFuture<Collection<Rank>> getRanks(@NotNull Account account) {
        return null;
      }

      @Override
      public @NotNull CompletableFuture<Collection<Rank<Platform>>> getRanks() {
        return null;
      }

      @Override
      public void applyRanks(@NotNull Account account, @NotNull Collection collection) {

      }
    }.isCaseSensitive(), is(true));

    assertThat(new RankResource<Platform>(false) {
      @Override
      public @NotNull CompletableFuture<Collection<Rank>> getRanks(@NotNull Account account) {
        return null;
      }

      @Override
      public @NotNull CompletableFuture<Collection<Rank<Platform>>> getRanks() {
        return null;
      }

      @Override
      public void applyRanks(@NotNull Account account, @NotNull Collection collection) {

      }
    }.isCaseSensitive(), is(false));
  }
}
