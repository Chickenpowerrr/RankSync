package com.gmail.chickenpowerrr.ranksync.discord.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.chickenpowerrr.ranksync.discord.DiscordPlatform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DiscordPlatformTest {

  private DiscordPlatform platform;

  @BeforeEach
  public void setUp() {
    this.platform = new DiscordPlatform("%name%");
  }

  @Test
  public void testValidFormat() {
    assertThat(this.platform.isValidFormat("@Test %name%")).isFalse();
    assertThat(this.platform.isValidFormat("Test ``` %name%")).isFalse();
    assertThat(this.platform.isValidFormat("@```@Test %name%")).isFalse();
    assertThat(this.platform.isValidFormat("this is a very long thing wit ha %name%")).isFalse();
    assertThat(this.platform.isValidFormat("this is exactly 33 chars %name%..")).isFalse();
    assertThat(this.platform.isValidFormat("%name%#6754")).isFalse();

    assertThat(this.platform.isValidFormat("[Rank] %name%")).isTrue();
    assertThat(this.platform.isValidFormat("Test: %name%")).isTrue();
    assertThat(this.platform.isValidFormat("%name%")).isTrue();
    assertThat(this.platform.isValidFormat("this is exactly 31 chars %name%")).isTrue();
    assertThat(this.platform.isValidFormat("this is exactly 32 chars %name%.")).isTrue();
  }

  @Test
  public void testValidName() {
    assertThat(this.platform.isValidFormat("@Test hi")).isFalse();
    assertThat(this.platform.isValidFormat("Test ``` hey")).isFalse();
    assertThat(this.platform.isValidFormat("@```@Test thing")).isFalse();
    assertThat(this.platform.isValidFormat("dong#6754")).isFalse();
    assertThat(this.platform.isValidFormat("this is 33 chars long, hopefully.")).isFalse();

    assertThat(this.platform.isValidFormat("[Rank] hihi")).isTrue();
    assertThat(this.platform.isValidFormat("Test: hoho")).isTrue();
    assertThat(this.platform.isValidFormat("Name")).isTrue();
    assertThat(this.platform.isValidFormat("this is exactly 31 chars right?")).isTrue();
    assertThat(this.platform.isValidFormat("this is exactly 32 chars right??")).isTrue();
  }
}
