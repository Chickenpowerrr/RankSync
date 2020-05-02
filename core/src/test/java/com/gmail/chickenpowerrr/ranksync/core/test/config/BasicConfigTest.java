package com.gmail.chickenpowerrr.ranksync.core.test.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gmail.chickenpowerrr.ranksync.core.config.BasicConfig;
import com.gmail.chickenpowerrr.ranksync.core.config.Config;
import com.gmail.chickenpowerrr.ranksync.core.config.exception.InvalidValueException;
import com.gmail.chickenpowerrr.ranksync.core.config.exception.UnknownKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class BasicConfigTest {

  private Config config;

  @BeforeEach
  public void setUp() {
    this.config = new BasicConfig();
  }

  @Test
  public void testSetUp() {
    assertThatThrownBy(() -> this.config.get("what")).isInstanceOf(UnknownKeyException.class);
    assertThatThrownBy(() -> this.config.getInt("hey")).isInstanceOf(UnknownKeyException.class);
    assertThatThrownBy(() -> this.config.getString("some")).isInstanceOf(UnknownKeyException.class);
    assertThatThrownBy(() -> this.config.getBoolean("idk")).isInstanceOf(UnknownKeyException.class);
    assertThatThrownBy(() -> this.config.getLong("word")).isInstanceOf(UnknownKeyException.class);
    assertThatThrownBy(() -> this.config.getDouble("hi")).isInstanceOf(UnknownKeyException.class);
  }

  @Test
  public void testGetInvalid() {
    this.config.setInt("hey", 5);
    assertThatThrownBy(() -> this.config.getBoolean("hey")).isInstanceOf(InvalidValueException.class);

    this.config.setBoolean("idk", false);
    assertThatThrownBy(() -> this.config.getDouble("idk")).isInstanceOf(InvalidValueException.class);

    this.config.setLong("word", 102L);
    assertThatThrownBy(() -> this.config.getBoolean("word")).isInstanceOf(InvalidValueException.class);

    this.config.setDouble("hi", 54D);
    assertThatThrownBy(() -> this.config.getLong("hi")).isInstanceOf(InvalidValueException.class);
  }

  @Test
  public void testOverride() {
    Object object = new Object();
    this.config.set("what", object);
    assertThat(this.config.get("what")).isEqualTo(object);
    this.config.set("what", null);
    assertThatThrownBy(() -> this.config.get("what")).isInstanceOf(UnknownKeyException.class);

    this.config.setInt("hey", 1);
    assertThat(this.config.getInt("hey")).isEqualTo(1);
    this.config.set("hey", null);
    assertThatThrownBy(() -> this.config.getInt("hey")).isInstanceOf(UnknownKeyException.class);

    this.config.setString("some", "value");
    assertThat(this.config.getString("some")).isEqualTo("value");
    this.config.set("some", null);
    assertThatThrownBy(() -> this.config.getString("some")).isInstanceOf(UnknownKeyException.class);

    this.config.setBoolean("idk", true);
    assertThat(this.config.getBoolean("idk")).isTrue();
    this.config.set("idk", null);
    assertThatThrownBy(() -> this.config.getBoolean("idk")).isInstanceOf(UnknownKeyException.class);

    this.config.setLong("word", 5L);
    assertThat(this.config.getLong("word")).isEqualTo(5L);
    this.config.set("word", null);
    assertThatThrownBy(() -> this.config.getLong("word")).isInstanceOf(UnknownKeyException.class);

    this.config.setDouble("hi", 7D);
    assertThat(this.config.getDouble("hi")).isEqualTo(7D);
    this.config.set("hi", null);
    assertThatThrownBy(() -> this.config.getDouble("hi")).isInstanceOf(UnknownKeyException.class);
  }

  @Test
  public void testSet() {
    Object object = new Object();
    this.config.set("what", object);
    assertThat(this.config.get("what")).isEqualTo(object);

    this.config.setInt("hey", 1);
    assertThat(this.config.getInt("hey")).isEqualTo(1);

    this.config.setString("some", "value");
    assertThat(this.config.getString("some")).isEqualTo("value");

    this.config.setBoolean("idk", true);
    assertThat(this.config.getBoolean("idk")).isTrue();

    this.config.setLong("word", 5L);
    assertThat(this.config.getLong("word")).isEqualTo(5L);

    this.config.setDouble("hi", 7D);
    assertThat(this.config.getDouble("hi")).isEqualTo(7D);
  }
}
