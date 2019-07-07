package com.gmail.chickenpowerrr.ranksync.server.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class UpdateChecker {

  private final Version currentVersion;

  public UpdateChecker() {
    this.currentVersion = new Version(getClass().getPackage().getImplementationVersion());
  }

  public void check() {
    try {
      URL url = new URL(
          "https://api.spigotmc.org/legacy/update.php?resource=61393");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Mozilla/5.0");
      try (InputStream inputStream = connection.getInputStream();
          InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
        Version latestVersion = new Version(bufferedReader.lines().collect(Collectors.joining()));

        if (!this.currentVersion.isDevelopmentBuild()) {
          switch (this.currentVersion.compareToRelevance(latestVersion)) {
            case -1:
              sendNewerAvailable(latestVersion.toString());
              break;
            case 1:
              sendNotReleased(latestVersion.toString());
              break;
            default:
              break;
          }
        } else {
          sendDevelopmentBuild(latestVersion.toString());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendDevelopmentBuild(String latestVersion) {
    System.out.println("===================================");
    System.out.println("RankSync Info:");
    System.out.println("You are running a development build");
    System.out.println("Your current version: \"" + this.currentVersion + "\"");
    System.out.println("The latest stable version: \"" + latestVersion + "\"");
    System.out.println("Because of this there might be some bugs");
    System.out.println("Please report them to Chickenpowerrr if you find them");
    System.out.println("===================================");
  }

  private void sendNotReleased(String latestVersion) {
    System.out.println("===================================");
    System.out.println("RankSync Info:");
    System.out.println("You are running a not released build");
    System.out.println("Your current version: \"" + this.currentVersion + "\"");
    System.out.println("The latest released version: \"" + latestVersion + "\"");
    System.out.println("This means that there might be some undiscovered issues");
    System.out.println("===================================");
  }

  private void sendNewerAvailable(String latestVersion) {
    System.out.println("===================================");
    System.out.println("RankSync Info:");
    System.out.println("There is a new version available on Spigot");
    System.out.println("Your current version: \"" + this.currentVersion + "\"");
    System.out.println("The new version: \"" + latestVersion + "\"");
    System.out.println("You can download it here:");
    System.out.println("https://www.spigotmc.org/resources/ranksync.61393/");
    System.out.println("===================================");
  }
}
