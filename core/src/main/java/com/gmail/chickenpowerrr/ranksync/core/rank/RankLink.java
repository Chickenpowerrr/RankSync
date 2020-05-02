package com.gmail.chickenpowerrr.ranksync.core.rank;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This class represents a link between multiple {@code Rank}s
 * on different {@code Platform}s.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class RankLink {

  private final String identifier;
  private final String nameFormat;
  private final Map<Platform<?>, Collection<Rank<?>>> ranks;

  /**
   * Establishes a new link, based on the unique identifier
   * of the link, the format for the name sync and the ranks
   * which will be linked.
   *
   * @param identifier the unique identifier of this link
   * @param nameFormat the format for the name sync
   * @param ranks the ranks which will be linked
   */
  public RankLink(@NotNull String identifier, @NotNull String nameFormat,
      @NotNull Map<Platform<?>, Collection<Rank<?>>> ranks) {
    this.identifier = identifier;
    this.nameFormat = nameFormat;
    this.ranks = Collections.unmodifiableMap(ranks.entrySet()
        .stream()
        .filter(entry -> entry.getValue().size() > 0)
        .collect(Collectors.toMap(Map.Entry::getKey,
            entry -> Collections.unmodifiableCollection(entry.getValue()))));
  }

  /**
   * Returns the unique identifier of this link.
   */
  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Returns the format for the name sync.
   */
  @Contract(pure = true)
  @NotNull
  public String getNameFormat() {
    return this.nameFormat;
  }

  /**
   * Returns the ranks which will be linked.
   */
  @Contract(pure = true)
  @NotNull
  public Map<Platform<?>, Collection<Rank<?>>> getRanks() {
    return this.ranks;
  }

  /**
   * Returns the best priority for the linked {@code Rank}s,
   * which are available on the given {@code Platform} and
   * linked by this {@code RankLink}.
   *
   * @param platform the {@code Platform} which contains the
   *                 target {@code Rank}s
   * @return a value between 0 and Integer.MAX_VALUE if a
   *         {@code Rank} has been linked for the specified
   *         {@code Platform} by this {@code RankLink},
   *         -1 otherwise
   */
  @Range(from = -1, to = Integer.MAX_VALUE)
  @Contract(pure = true)
  public int getPriority(@NotNull Platform<?> platform) {
    return this.ranks.containsKey(platform) ? this.ranks.get(platform).stream()
        .mapToInt(Rank::getPriority)
        .min()
        .orElse(-1) : -1;
  }
}
