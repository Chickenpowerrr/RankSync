package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DiscordAccount extends Account<DiscordPlatform> {

  private final Member member;

  public DiscordAccount(@NotNull Member member, @NotNull DiscordPlatform platform,
      @NotNull List<UserLink<DiscordPlatform>> userLinks) {
    super(member.getId(), platform, userLinks);

    this.member = member;
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public String getName() {
    return this.member.getEffectiveName();
  }

  @Override
  public void updateRanks(@NotNull List<Rank<DiscordPlatform>> ranks) {
    getPlatform().getRanks().whenComplete((supportedRanks, throwable1) -> {
      try {
        if (throwable1 != null) {
          throwable1.printStackTrace();
          return;
        }

        Guild guild = this.member.getGuild();

        List<Role> newRoles = new ArrayList<>(this.member.getRoles());
        newRoles.removeAll(getRoles(guild, supportedRanks));
        newRoles.addAll(getRoles(guild, ranks));

        guild.modifyMemberRoles(this.member, newRoles).queue();
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    });
  }

  @Override
  public boolean updateName(@NotNull String name) {
    try {
      this.member.modifyNickname(name).queue();
      return true;
    } catch (HierarchyException e) {
      return false;
    }
  }

  @Contract(pure = true)
  public Member getMember() {
    return this.member;
  }

  @Contract(pure = true)
  private Collection<Role> getRoles(@NotNull Guild guild,
      @NotNull Collection<Rank<DiscordPlatform>> supportedRanks) {
    return supportedRanks.stream()
        .map(rank -> guild.getRoleById(rank.getIdentifier()))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
