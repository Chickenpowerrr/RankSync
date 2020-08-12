package com.gmail.chickenpowerrr.ranksync.core.config;

import com.gmail.chickenpowerrr.ranksync.core.config.exception.InvalidValueException;
import com.gmail.chickenpowerrr.ranksync.core.config.exception.UnknownKeyException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This {@link Config} uses a {@link ValueProvider} to save
 * and load values.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class AbstractConfig implements Config {

  private final ValueProvider valueProvider;

  /**
   * Constructs a new {@link Config} which uses the provided
   * {@link ValueProvider} as its backend storage.
   *
   * @param valueProvider the {@link ValueProvider} which should
   *                      store and load requested variables
   */
  public AbstractConfig(@NotNull ValueProvider valueProvider) {
    this.valueProvider = valueProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Contract(value = "null -> false", pure = true)
  @Override
  public boolean contains(String path) {
    return this.valueProvider.get(path) != null;
  }

  /**
   * {@inheritDoc}
   * @throws UnknownKeyException if there is no value at the
   *                             given path
   */
  @Contract(pure = true)
  @Override
  public Object get(@Nullable String path) {
    if (path == null) {
      return null;
    }

    if (contains(path)) {
      return this.valueProvider.get(path);
    } else {
      throw new UnknownKeyException(path);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void set(@NotNull String path, Object value) {
    this.valueProvider.set(path, value);
  }

  /**
   * {@inheritDoc}
   * @throws UnknownKeyException if there is no value at the
   *                             given path
   */
  @Contract(pure = true)
  @Override
  public String getString(@NotNull String path) {
    return get(path).toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setString(@NotNull String path, String value) {
    set(path, value);
  }

  /**
   * {@inheritDoc}
   */
  @Contract(pure = true)
  @Override
  public boolean isInt(String path) {
    return contains(path) && (get(path).getClass().equals(Integer.class)
        || getString(path).matches("^-?\\d+$"));
  }

  /**
   * {@inheritDoc}
   * @throws UnknownKeyException if there is no value at the
   *                             given path
   * @throws InvalidValueException if the value at the given path
   *                               isn't an integer
   */
  @Contract(pure = true)
  @Override
  public int getInt(@NotNull String path) {
    if (isInt(path)) {
      Object target = get(path);

      if (target instanceof Integer) {
        return (int) target;
      } else {
        return Integer.parseInt(target.toString());
      }
    } else {
      if (contains(path)) {
        throw new InvalidValueException(path, Integer.class);
      } else {
        throw new UnknownKeyException(path);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInt(@NotNull String path, int value) {
    set(path, value);
  }

  /**
   * {@inheritDoc}
   */
  @Contract(pure = true)
  @Override
  public boolean isDouble(@Nullable String path) {
    return contains(path) && (get(path).getClass().equals(Double.class)
        || getString(path).matches("^-?(\\d+\\.?\\d*)|(\\d*\\.?\\d+)$"));
  }

  /**
   * {@inheritDoc}
   * @throws UnknownKeyException if there is no value at the
   *                             given path
   * @throws InvalidValueException if the value at the given path
   *                               isn't a double
   */
  @Contract(pure = true)
  @Override
  public double getDouble(@NotNull String path) {
    if (isDouble(path)) {
      Object target = get(path);

      if (target instanceof Double) {
        return (double) target;
      } else {
        return Double.parseDouble(target.toString());
      }
    } else {
      if (contains(path)) {
        throw new InvalidValueException(path, Double.class);
      } else {
        throw new UnknownKeyException(path);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDouble(@NotNull String path, double value) {
    set(path, value);
  }

  /**
   * {@inheritDoc}
   */
  @Contract(pure = true)
  @Override
  public boolean isLong(String path) {
    return contains(path) && (get(path).getClass().equals(Long.class)
        || getString(path).matches("^-?\\d+$"));
  }

  /**
   * {@inheritDoc}
   * @throws UnknownKeyException if there is no value at the
   *                             given path
   * @throws InvalidValueException if the value at the given path
   *                               isn't a long
   */
  @Contract(pure = true)
  @Override
  public long getLong(@NotNull String path) {
    if (isLong(path)) {
      Object target = get(path);

      if (target instanceof Long) {
        return (long) target;
      } else {
        return Long.parseLong(target.toString());
      }
    } else {
      if (contains(path)) {
        throw new InvalidValueException(path, Long.class);
      } else {
        throw new UnknownKeyException(path);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLong(@NotNull String path, long value) {
    set(path, value);
  }

  /**
   * {@inheritDoc}
   */
  @Contract(pure = true)
  @Override
  public boolean isBoolean(String path) {
    return contains(path) && (get(path).getClass().equals(Boolean.class)
        || getString(path).matches("^(true)|(false)$"));
  }

  /**
   * {@inheritDoc}
   * @throws UnknownKeyException if there is no value at the
   *                             given path
   * @throws InvalidValueException if the value at the given path
   *                               isn't a boolean
   */
  @Contract(pure = true)
  @Override
  public boolean getBoolean(@NotNull String path) {
    if (isBoolean(path)) {
      Object target = get(path);

      if (target instanceof Boolean) {
        return (boolean) target;
      } else {
        return Boolean.parseBoolean(target.toString());
      }
    } else {
      if (contains(path)) {
        throw new InvalidValueException(path, Boolean.class);
      } else {
        throw new UnknownKeyException(path);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBoolean(@NotNull String path, boolean value) {
    set(path, value);
  }
}
