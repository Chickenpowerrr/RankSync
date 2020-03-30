package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class RoleRankResource extends RankResource<DiscordPlatform> {

  private final Guild guild;

  public RoleRankResource(@NotNull Guild guild) {
    super(false);

    this.guild = guild;
  }

  @NotNull
  @Override
  public CompletableFuture<Collection<Rank<DiscordPlatform>>> getRanks() {
    // TODO implement
    return null;
  }

  @NotNull
  @Override
  public CompletableFuture<Collection<Rank<DiscordPlatform>>> getRanks(
      @NotNull Account<DiscordPlatform> account) {
    // TODO implement
    return null;
  }

  @Override
  public void applyRanks(@NotNull Account<DiscordPlatform> account,
      @NotNull Collection<Rank<DiscordPlatform>> ranks) {
    // TODO implement
  }
}
