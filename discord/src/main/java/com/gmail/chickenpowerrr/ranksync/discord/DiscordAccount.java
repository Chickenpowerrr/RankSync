package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

public class DiscordAccount extends Account<DiscordPlatform> {

  private final Member member;

  public DiscordAccount(@NotNull Member member, @NotNull DiscordPlatform platform,
      @NotNull List<UserLink<DiscordPlatform>> userLinks) {
    super(member.getId(), platform, userLinks);

    this.member = member;
  }

  @NotNull
  @Override
  public String getName() {
    return this.member.getEffectiveName();
  }

  @NotNull
  @Override
  public CompletableFuture<String> formatName() {
    return null;
  }

  @Override
  public void updateRanks(@NotNull List<Rank<DiscordPlatform>> ranks) {
    getPlatform().getRanks().whenComplete((supportedRanks, throwable1) -> {
      if (throwable1 != null) {
        throwable1.printStackTrace();
        return;
      }

      Guild guild = this.member.getGuild();
      getRoles(guild, supportedRanks).whenComplete((newRoles, throwable2) -> {
        if (throwable2 != null) {
          throwable2.printStackTrace();
          return;
        }

        guild.modifyMemberRoles(this.member, newRoles).queue();
      });
    });
  }

  @Override
  public boolean updateName(@NotNull String name) {
    this.member.modifyNickname(name).queue();
    // TODO implement
    return false;
  }

  private CompletableFuture<Collection<Role>> getRoles(@NotNull Guild guild,
      @NotNull Collection<Rank<DiscordPlatform>> supportedRanks) {
    List<Role> result = this.member.getRoles();
    supportedRanks.stream().map(rank -> guild.getRoleById(rank.getIdentifier()))
        .forEach(result::remove);
    return getPlatform().getRanks(this).thenApply(ranks -> {
      ranks.stream().map(rank -> guild.getRoleById(rank.getIdentifier()))
          .forEach(result::add);
      return result;
    });
  }
}
