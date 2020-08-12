package com.gmail.chickenpowerrr.ranksync.core.rank;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This class represents a {@code Rank} on a certain
 * {@code Platform} which can be linked to an {@code Account}.
 *
 * @param <T> the type of the {@code Platform} on which
 *            the {@code Rank} is available
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class Rank<T extends Platform<T>> implements Comparable<Rank<?>> {

  private final String identifier;
  private final int priority;
  private final String type;
  private final String name;

  /**
   * Initializes a {@code Rank} based on the identifier,
   * name and priority of the {@code Rank}.
   *
   * @param identifier the identifier of the {@code Rank}
   * @param name the name of the {@code Rank}
   * @param type the shared type which is for the {@link RankResource}
   * @param priority the priority of the {@code Rank}
   */
  public Rank(@NotNull String identifier, @NotNull String name, @NotNull String type,
      int priority) {
    this.identifier = identifier;
    this.priority = priority;
    this.type = type;
    this.name = name;
  }

  /**
   * Returns the unique identifier of the {@code Rank}
   * on its {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Returns the priority of the {@code Rank} which will be
   * used to determine which {@code Rank} will be displayed
   * first. A lower priority results in the {@code Rank}
   * being more important.
   */
  @Range(from = 0, to = Integer.MAX_VALUE)
  @Contract(pure = true)
  public int getPriority() {
    return this.priority;
  }

  /**
   * Returns the name of the {@code Rank} on the {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Returns the shared type which is for the {@link RankResource}.
   */
  @Contract(pure = true)
  @NotNull
  public String getType() {
    return this.type;
  }

  /**
   * Compare two {@link Rank}s by their priority.
   *
   * @param rank the target {@link Rank}
   * @return -1 if the current priority is lower, 0 if they are equal
   *         and 1 if the other one is bigger
   */
  @Override
  public int compareTo(@NotNull Rank<?> rank) {
    return Integer.compare(this.priority, rank.priority);
  }
}
