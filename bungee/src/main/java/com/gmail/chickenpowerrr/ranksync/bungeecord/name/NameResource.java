package com.gmail.chickenpowerrr.ranksync.bungeecord.name;

import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * This class looks up the last known name of a user by their UUID
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public class NameResource implements com.gmail.chickenpowerrr.ranksync.api.name.NameResource {

  private final Plugin plugin;
  private final JsonParser jsonParser = new JsonParser();

  /**
   * Saves the plugin instance
   *
   * @param plugin the plugin instance
   */
  public NameResource(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Gets the name of the player if they're online, otherwise asks the Mojang API for the current
   * username
   *
   * @param uuid the UUID on the platform the name should be retrieved from
   */
  @Override
  public String getName(UUID uuid) {
    ProxiedPlayer proxiedPlayer = this.plugin.getProxy().getPlayer(uuid);
    if (proxiedPlayer == null) {
      try {
        URL url = new URL(
            "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString()
                .replace("-", ""));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(connection.getInputStream()))) {
          return this.jsonParser.parse(bufferedReader).getAsJsonObject().get("name").getAsString();
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      return proxiedPlayer.getName();
    }
  }
}
