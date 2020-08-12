package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BoostRankResource extends RankResource<DiscordPlatform> {

  public static final String BOOST_TYPE = "boost";

  private final Guild guild;
  private final Collection<Rank<DiscordPlatform>> boostedRanks;

  public BoostRankResource(@NotNull Guild guild, Collection<Rank<DiscordPlatform>> allRanks) {
    super(false);

    this.guild = guild;;
    this.boostedRanks = allRanks.stream()
        .filter(rank -> rank.getType().equals(BOOST_TYPE))
        .collect(Collectors.toList());
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public CompletableFuture<Collection<Rank<DiscordPlatform>>> getRanks() {
    return CompletableFuture.completedFuture(this.boostedRanks);
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public CompletableFuture<Collection<Rank<DiscordPlatform>>> getRanks(
      @NotNull Account<DiscordPlatform> account) {
    Member member = this.guild.getMemberById(account.getIdentifier());
    if (member != null && member.getTimeBoosted() != null) {
      return CompletableFuture.completedFuture(this.boostedRanks);
    } else {
      return CompletableFuture.completedFuture(Collections.emptyList());
    }
  }

  @Override
  public void applyRanks(@NotNull Account<DiscordPlatform> account,
      @NotNull Collection<Rank<DiscordPlatform>> ranks) {
    // Cannot be done
  }
}
