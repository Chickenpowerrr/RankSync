package com.gmail.chickenpowerrr.ranksync.discord.player;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.event.BotPlayerRanksUpdateEvent;
import com.gmail.chickenpowerrr.ranksync.discord.rank.RankFactory;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class represents a Discord user
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class Player implements com.gmail.chickenpowerrr.ranksync.api.player.Player {

  @Getter @Setter private UUID uuid;
  private final Member member;
  private final RankFactory rankFactory;
  @Getter private final Bot bot;

  Player(UUID uuid, Member member, Bot<Member, ?> bot) {
    this.uuid = uuid;
    this.member = member;
    this.rankFactory = RankFactory.getInstance(member.getGuild());
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
   * Synchronizes the ranks of the Discord user with the other platforms
   */
  @Override
  public void updateRanks() {
    if (this.uuid != null) {
      this.bot.getEffectiveDatabase().getRanks(this.uuid).thenAccept(ranks -> {
        if (ranks != null) {
          setRanks(ranks);
        }
      });
    } else {
      setRanks(new HashSet<>());
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
      this.member.getGuild().getController().removeRolesFromMember(this.member, toRemove).submit();
      this.member.getGuild().getController().addRolesToMember(this.member, toAdd).submit();
    }
  }
}
