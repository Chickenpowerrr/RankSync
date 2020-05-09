package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public class DiscordRank extends Rank<DiscordPlatform> {

  private final Role role;

  public DiscordRank(@NotNull Role role, int priority) {
    super(role.getId(), role.getName(), priority);

    this.role = role;
  }

  @TestOnly
  public Role getRole() {
    return this.role;
  }
}
