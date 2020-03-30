package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.entities.Member;
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
  public CompletableFuture<String> formatName(@NotNull Account<DiscordPlatform> account) {
    return null;
  }

  @Override
  public boolean updateName(@NotNull String name) {
    this.member.modifyNickname(name).queue();
    // TODO implement
    return false;
  }
}
