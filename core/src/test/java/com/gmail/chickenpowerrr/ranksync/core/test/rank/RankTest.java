package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RankTest {

  private static final String IDENTIFIER = "identifier";
  private static final String TYPE = "test type";
  private static final String NAME = "name";
  private static final int PRIORITY = 5;

  private Rank<TestPlatform> rank;

  @BeforeEach
  public void setUp() {
    this.rank = new Rank<>(IDENTIFIER, NAME, TYPE, PRIORITY);
  }

  @Test
  public void testSetup() {
    assertThat(this.rank.getIdentifier()).isEqualTo(IDENTIFIER);
    assertThat(this.rank.getName()).isEqualTo(NAME);
    assertThat(this.rank.getPriority()).isEqualTo(PRIORITY);
  }
}
