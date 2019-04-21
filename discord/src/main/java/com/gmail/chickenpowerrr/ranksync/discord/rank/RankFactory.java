package com.gmail.chickenpowerrr.ranksync.discord.rank;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

/**
 * This class contains the functionalities to get a representation of a Discord role
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class RankFactory implements com.gmail.chickenpowerrr.ranksync.api.rank.RankFactory<Role> {

  private static final Map<Guild, RankFactory> instances = new HashMap<>();

  private final Map<Role, Rank> ranks = new HashMap<>();
  private final Map<String, Role> roles = new HashMap<>();
  private final Guild guild;
  @Getter
  private final Bot<?, Role> bot;
  private final Collection<RankHelper> rankHelpers = new HashSet<>();

  /**
   * @param bot the Bot that is running
   * @param guild the Guild the Bot is running on
   */
  private RankFactory(Bot<?, Role> bot, Guild guild) {
    this.bot = bot;
    this.guild = guild;
  }

  /**
   * Lazily returns an instance based on the Guild the Bot is running on
   *
   * @param bot the Bot that is running
   * @param guild the Guild the Bot is running on
   * @return an instance based on the Guild the Bot is running on
   */
  public static RankFactory getInstance(Bot<?, Role> bot, Guild guild) {
    if (!instances.containsKey(guild)) {
      if (guild != null) {
        instances.put(guild, new RankFactory(bot, guild));
      }
    }
    return instances.get(guild);
  }

  /**
   * Lazily returns an instance based on the Guild the Bot is running on
   *
   * @param guild the Guild the Bot is running on
   * @return an instance based on the Guild the Bot is running on
   */
  public static RankFactory getInstance(Guild guild) {
    return getInstance(null, guild);
  }

  /**
   * Turns the JDA Role into a rank that is supported by the RankSync API
   *
   * @param role the Role object used by JDA, not the RankSync API
   * @return the RankSync representation of the Role
   */
  @Override
  public Rank getRankFromRole(Role role) {
    if (role != null) {
      if (!this.ranks.containsKey(role)) {
        this.ranks.put(role, new com.gmail.chickenpowerrr.ranksync.discord.rank.Rank(role));
      }
      return this.ranks.get(role);
    } else {
      return null;
    }
  }

  /**
   * Turns the JDA Roles that are supported by the RankSync API
   *
   * @param roles the Role objects used by JDA, not the RankSync API
   * @return the RankSync representations of the Roles
   */
  @Override
  public Collection<Rank> getRanksFromRoles(Collection<Role> roles) {
    return roles.stream().map(this::getRankFromRole).collect(Collectors.toSet());
  }

  /**
   * Returns the JDA Role by its name
   *
   * @param string the name of the role
   * @return the Role used by JDA, not the RankSync API
   */
  @Override
  public Role getRoleFromName(String string) {
    if (string != null) {
      if (!this.roles.containsKey(string)) {
        List<Role> roles = this.guild.getRolesByName(string, true);
        if (!roles.isEmpty()) {
          this.roles.put(string, roles.get(0));
        } else {
          return null;
        }
      }
      return this.roles.get(string);
    } else {
      return null;
    }
  }

  /**
   * Returns the JDA Roles by their names
   *
   * @param strings the names of the roles
   * @return the Roles used by JDA, not the RankSync API
   */
  @Override
  public Collection<Role> getRolesFromNames(Collection<String> strings) {
    return strings.stream().map(this::getRoleFromName).collect(Collectors.toSet());
  }

  /**
   * Turns the role supported by the RankSync API back into the JDA Role
   *
   * @param rank the rank that can by used by the RankSync API
   * @return the JDA Role
   */
  @Override
  public Role getRoleFromRank(Rank rank) {
    if (rank instanceof com.gmail.chickenpowerrr.ranksync.discord.rank.Rank) {
      return ((com.gmail.chickenpowerrr.ranksync.discord.rank.Rank) rank).getRole();
    } else {
      return getRoleFromName(rank.getName());
    }
  }

  /**
   * Turns the roles supported by the RankSync API back into the JDA Roles
   *
   * @param ranks the ranks that can by used by the RankSync API
   * @return the JDA Roles
   */
  @Override
  public Collection<Role> getRolesFromRanks(Collection<Rank> ranks) {
    return ranks.stream().map(this::getRoleFromRank).collect(Collectors.toSet());
  }

  /**
   * Adds a RankHelper to validate all ranks
   *
   * @param rankHelper the helper that validates if the given ranks exist
   */
  @Override
  public void addRankHelper(RankHelper rankHelper) {
    this.rankHelpers.add(rankHelper);
  }

  /**
   * Returns if the given rank is a valid platform rank according to the RankHelpers
   *
   * @param rank the rank that could be synchronized
   * @return if the given rank is a valid platform rank according to the RankHelpers
   */
  @Override
  public boolean isValidRank(Rank rank) {
    return this.rankHelpers.stream()
        .anyMatch(rankHelper -> rankHelper.isSynchronized(this.bot, rank));
  }
}
