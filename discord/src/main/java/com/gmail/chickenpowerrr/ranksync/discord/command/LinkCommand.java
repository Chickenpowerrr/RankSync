package com.gmail.chickenpowerrr.ranksync.discord.command;

import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.command.Command;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkCodeCreateEvent;
import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import com.gmail.chickenpowerrr.ranksync.discord.language.Translation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * This class represents the Discord !link command, it can be used to link a Discord account to
 * another service
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class LinkCommand implements Command {

  private static final Random random = new Random();

  @Getter
  private final String label;
  private final Collection<String> aliases;

  private final Map<String, Long> timeOuts = new HashMap<>();

  /**
   * Starts a timer that deletes codes that haven't been used after five minutes
   *
   * @param label the label of the command ('link')
   * @param aliases all aliases that, besides the name can get used to invoke this command
   */
  public LinkCommand(String label, Collection<String> aliases) {
    this.label = label;
    this.aliases = aliases;

    new Timer().scheduleAtFixedRate(new TimerTask() {

      @Override
      public void run() {
        Collection<String> toRemove = timeOuts.entrySet().stream()
            .filter(entry -> entry.getValue() + 1000 * 60 * 5 < System.currentTimeMillis())
            .map(Map.Entry::getKey).collect(Collectors.toSet());
        toRemove.forEach(timeOuts::remove);
      }
    }, 1000 * 10, 1000 * 10);
  }

  /**
   * Links the account of a Discord user if it hasn't been linked already
   *
   * @param invoker the Discord user that invokes the command
   * @param arguments should be empty for this command
   * @return the message that will be send into the channel where the command was executed
   */
  @Override
  public String execute(Player invoker, List<String> arguments) {
    if (invoker.getUuid() == null) {
      if (!onCooldown(invoker.getPersonalId())) {
        String secretKey = randomString(10 + random.nextInt(2));
        RankSyncApi.getApi().execute(new PlayerLinkCodeCreateEvent(invoker, secretKey));

        if (invoker.sendPrivateMessage(
            Translation.LINK_COMMAND_PRIVATE
                .getTranslation("name", invoker.getFancyName(), "key", secretKey))) {
          this.timeOuts.put(invoker.getPersonalId(), System.currentTimeMillis());
          return Translation.LINK_COMMAND_PUBLIC.getTranslation("name", invoker.getFancyName());
        } else {
          return Translation.LINK_COMMAND_ENABLE_PRIVATE_MESSAGES
              .getTranslation("name", invoker.getFancyName());
        }
      } else {
        if (invoker.sendPrivateMessage(Translation.LINK_COMMAND_RIGHTTHERE.getTranslation())) {
          return Translation.LINK_COMMAND_REQUEST_LIMIT
              .getTranslation("name", invoker.getFancyName());
        } else {
          return Translation.LINK_COMMAND_ENABLE_PRIVATE_MESSAGES
              .getTranslation("name", invoker.getFancyName());
        }
      }
    } else {
      return Translation.LINK_COMMAND_ALREADY_LINKED.getTranslation("name", invoker.getFancyName());
    }
  }

  /**
   * Returns if the user is still on cooldown
   *
   * @param identifier the identifier of the Discord user
   * @return if the user is still on cooldown
   */
  private boolean onCooldown(String identifier) {
    return this.timeOuts.containsKey(identifier)
        && this.timeOuts.get(identifier) + 1000 * 60 * 5 >= System.currentTimeMillis();
  }

  /**
   * Returns true because everyone can link their account
   *
   * @param player the player who wants to link their Discord account
   * @return true
   */
  @Override
  public boolean hasPermission(Player player) {
    return true;
  }

  @Override
  public Collection<String> getAliases() {
    return Collections.unmodifiableCollection(this.aliases);
  }

  /**
   * Returns a random String with the given size
   *
   * @param size the amount of characters in the String
   * @return a random String with the requested size
   */
  private String randomString(int size) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < size; i++) {
      stringBuilder.append(randomChar());
    }
    return stringBuilder.toString();
  }

  /**
   * Returns a random character
   */
  private char randomChar() {
    int randomNumber = random.nextInt(52);
    char base = (randomNumber < 26) ? 'A' : 'a';
    return (char) (base + randomNumber % 26);
  }
}
