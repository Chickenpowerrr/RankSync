package com.gmail.chickenpowerrr.ranksync.discord.player;

import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.event.BotPlayerRanksUpdateEvent;
import com.gmail.chickenpowerrr.ranksync.api.name.NameResource;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.discord.rank.RankFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;

/**
 * This class represents a Discord user
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class Player implements com.gmail.chickenpowerrr.ranksync.api.player.Player {

  private final Member member;
  private final RankFactory rankFactory;
  private final NameResource nameResource;

  @Getter
  private final Bot bot;

  @Getter
  @Setter
  private UUID uuid;

  Player(UUID uuid, Member member, Bot<Member, ?> bot) {
    this.uuid = uuid;
    this.member = member;
    this.rankFactory = RankFactory.getInstance(member.getGuild());
    this.nameResource = bot.getNameResource();
    this.bot = bot;
  }

  /**
   * Returns the effective name of the Discord user
   */
  @Override
  public String getFancyName() {
    return this.member.getEffectiveName();
  }

  /**
   * Returns the current Discord ranks the user has attached to its account
   */
  @Override
  public Collection<Rank> getRanks() {
    return this.member.getGuild().getRoles().stream().map(this.rankFactory::getRankFromRole)
        .collect(Collectors.toSet());
  }

  /**
   * Return the user's Discord identifier
   */
  @Override
  public String getPersonalId() {
    return this.member.getUser().getId();
  }

  /**
   * Sends a DM to the user
   *
   * @param message the message the user should receive
   */
  @Override
  public void sendPrivateMessage(String message) {
    this.member.getUser().openPrivateChannel()
        .queue((privateChannel -> privateChannel.sendMessage(message).queue()));
  }

  /**
   * Synchronizes the ranks and the name of the Discord user with the other platforms
   */
  @Override
  public void update() {
    if (this.uuid != null) {
      this.bot.getEffectiveDatabase().getRanks(this.uuid).thenAccept(ranks -> {
        if (ranks != null) {
          setRanks(ranks);
        }

        if (this.bot.doesUpdateNames()) {
          String username = this.nameResource.getName(this.uuid);
          if (ranks != null) {
            Optional<Rank> rank = ranks.stream().findFirst();
            if (rank.isPresent()) {
              setUsername(this.bot.getNameSyncFormat()
                  .replace("%name%", username)
                  .replace("%discord_rank%", rank.get().getName()));
            } else {
              setUsername(this.bot.getNameSyncFormat().replace("%name%", username));
            }
          } else {
            setUsername(this.bot.getNameSyncFormat().replace("%name%", username));
          }
        }
      });
    } else {
      if (this.bot.doesUpdateNonSynced()) {
        setRanks(new HashSet<>());
      }
    }
  }

  /**
   * Returns the name of the user
   */
  @Override
  public String getUsername() {
    return this.nameResource.getName(this.uuid);
  }

  /**
   * Updates the name of the current player
   *
   * @param username the new username
   */
  @Override
  public void setUsername(String username) {
    try {
      this.member.modifyNickname(username).queue();
    } catch (HierarchyException e) {
      if (this.rankFactory.shouldThrowPermissionWarnings()) {
        System.out.println(
            "Can't update the username for Discord user: " + this.member.getEffectiveName()
                + " since their rank is too high");
      }
    }
  }

  /**
   * Sets the ranks of the Discord user
   *
   * @param ranks the ranks the Discord user should have
   */
  @Override
  public void setRanks(Collection<Rank> ranks) {
    Collection<Role> roles = this.member.getRoles();

    Collection<Role> toRemove = new HashSet<>(roles);
    toRemove.removeIf(role -> {
      Rank rank = this.rankFactory.getRankFromRole(role);
      return ranks.contains(rank) || !this.rankFactory.isValidRank(rank);
    });

    Collection<Role> toAdd = this.rankFactory.getRolesFromRanks(ranks);
    toAdd.removeIf(roles::contains);

    if (!RankSyncApi.getApi().execute(new BotPlayerRanksUpdateEvent(this, this.rankFactory.getBot(),
        toRemove.stream().map(this.rankFactory::getRankFromRole).collect(Collectors.toSet()),
        toAdd.stream().map(this.rankFactory::getRankFromRole).collect(Collectors.toSet())))
        .cancelled()) {
      toRemove
          .forEach(role -> this.member.getGuild().removeRoleFromMember(this.member, role).queue());
      toAdd
          .forEach(role -> this.member.getGuild().addRoleToMember(this.member, role).queue());
    }
  }
}
