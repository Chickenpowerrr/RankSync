package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RoleRankResource extends RankResource<DiscordPlatform> {

  public static final String ROLE_TYPE = "role";

  private final Guild guild;
  private final Collection<Rank<DiscordPlatform>> allRanks;

  public RoleRankResource(@NotNull Guild guild,
      @NotNull Collection<Rank<DiscordPlatform>> allRanks) {
    super(false);

    this.guild = guild;
    this.allRanks = allRanks;
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public CompletableFuture<Collection<Rank<DiscordPlatform>>> getRanks() {
    return CompletableFuture.completedFuture(this.allRanks.stream()
        .filter(rank -> rank.getType().equals(ROLE_TYPE))
        .filter(rank -> this.guild.getRoleById(rank.getIdentifier()) != null)
        .collect(Collectors.toList()));
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public CompletableFuture<Collection<Rank<DiscordPlatform>>> getRanks(
      @NotNull Account<DiscordPlatform> account) {
    Member member = this.guild.getMemberById(account.getIdentifier());
    if (member != null) {
      Collection<String> roles = member.getRoles().stream()
          .map(Role::getId)
          .collect(Collectors.toSet());

      return CompletableFuture.completedFuture(this.allRanks.stream()
          .filter(rank -> rank.getType().equals(ROLE_TYPE))
          .filter(rank -> roles.contains(rank.getIdentifier()))
          .collect(Collectors.toList()));
    } else {
      return CompletableFuture.completedFuture(Collections.emptyList());
    }
  }

  @Override
  public void applyRanks(@NotNull Account<DiscordPlatform> account,
      @NotNull Collection<Rank<DiscordPlatform>> ranks) {
    Member member = this.guild.getMemberById(account.getIdentifier());

    if (member != null) {
      this.guild.modifyMemberRoles(member, ranks.stream()
          .filter(rank -> rank.getType().equals(ROLE_TYPE))
          .map(rank -> this.guild.getRoleById(rank.getIdentifier()))
          .filter(Objects::nonNull)
          .collect(Collectors.toList())).queue();
    }
  }
}
