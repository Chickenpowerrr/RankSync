package com.gmail.chickenpowerrr.ranksync.discord.rank;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

/**
 * This class represents a Discord role
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class Rank implements com.gmail.chickenpowerrr.ranksync.api.rank.Rank {

  @Getter private final Role role;

  /**
   * Wraps the role based on the name and the guild
   *
   * @param guild the guild of the role
   * @param name the name of the role
   */
  Rank(Guild guild, String name) {
    this.role = guild.getRolesByName(name, true).get(0);
  }

  /**
   * Wraps the role based on the Discord role
   *
   * @param role the Discord role
   */
  Rank(Role role) {
    this.role = role;
  }

  /**
   * Returns the name of the Discord role
   */
  @Override
  public String getName() {
    return this.role.getName();
  }

  @Override
  public String toString() {
    return "{RANK:" + getName() + "}";
  }
}
