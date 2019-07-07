package com.gmail.chickenpowerrr.ranksync.server.update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode
public class Version {

  private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d");

  private final boolean isAlphaVersion;
  private final boolean isBetaVersion;

  private final int major;
  private final int minor;
  private final int patch;

  public Version(String versionString) {
    this.isAlphaVersion = versionString.toLowerCase().contains("alpha");
    this.isBetaVersion = versionString.toLowerCase().contains("beta");

    Matcher versionMatcher = NUMBER_PATTERN.matcher(versionString);

    versionMatcher.find();
    this.major = Integer.valueOf(versionMatcher.group());

    versionMatcher.find();
    this.minor = Integer.valueOf(versionMatcher.group());

    versionMatcher.find();
    this.patch = Integer.valueOf(versionMatcher.group());
  }

  public boolean isDevelopmentBuild() {
    return this.isAlphaVersion || this.isBetaVersion;
  }

  public int compareToRelevance(@NotNull Version version) {
    if (isDevelopmentBuild()) {
      if (version.isAlphaVersion || version.isBetaVersion) {
        return compareToRelease(version);
      } else {
        return -1;
      }
    } else {
      if (version.isDevelopmentBuild()) {
        return 1;
      } else {
        return compareToRelease(version);
      }
    }
  }

  public int compareToRelease(@NotNull Version version) {
    if (!this.equals(version)) {
      if (this.major == version.major) {
        if (this.minor == version.minor) {
          if (this.patch == version.patch) {
            if (isDevelopmentBuild()) {
              if (version.isDevelopmentBuild()) {
                if (this.isBetaVersion) {
                  return 1;
                } else {
                  return -1;
                }
              } else {
                return -1;
              }
            } else {
              return 1;
            }
          } else {
            return Integer.compare(this.patch, version.patch);
          }
        } else {
          return Integer.compare(this.minor, version.minor);
        }
      } else {
        return Integer.compare(this.major, version.major);
      }
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return this.major + "." + this.minor + "." + this.patch
        + (this.isAlphaVersion ? "-ALPHA" : "")
        + (this.isBetaVersion ? "-BETA" : "");
  }
}
