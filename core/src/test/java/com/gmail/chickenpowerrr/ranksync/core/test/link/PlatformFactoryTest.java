package com.gmail.chickenpowerrr.ranksync.core.test.link;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.chickenpowerrr.ranksync.core.config.BasicConfig;
import com.gmail.chickenpowerrr.ranksync.core.link.PlatformFactory;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlatformFactoryTest {

  private static final String PLATFORM_NAME = "platform";

  private PlatformFactory platformFactory;

  @BeforeEach
  public void setUp() {
    this.platformFactory = new PlatformFactory();
  }

  @Test
  public void testSetUp() {
    assertThat(this.platformFactory.<TestPlatform>getPlatform(PLATFORM_NAME, new BasicConfig()))
        .isNull();
  }

  @Test
  public void testNullInput() {
    this.platformFactory.register(PLATFORM_NAME, config -> new TestPlatform());
    assertThat(this.platformFactory.<TestPlatform>getPlatform(null, new BasicConfig())).isNull();
    assertThat(this.platformFactory.<TestPlatform>getPlatform(PLATFORM_NAME, null)).isNull();
    assertThat(this.platformFactory.<TestPlatform>getPlatform(null, null)).isNull();
  }

  @Test
  public void testRegister() {
    this.platformFactory.register(PLATFORM_NAME, config -> new TestPlatform());
    assertThat(this.platformFactory.<TestPlatform>getPlatform(PLATFORM_NAME, new BasicConfig()))
        .isInstanceOf(TestPlatform.class);
  }
}
