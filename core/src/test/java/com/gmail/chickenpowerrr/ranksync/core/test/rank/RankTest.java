package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RankTest {

  private static final String IDENTIFIER = "identifier";
  private static final String NAME = "name";
  private static final int PRIORITY = 5;

  private Rank rank;

  @BeforeEach
  public void setUp() {
    this.rank = new Rank(IDENTIFIER, NAME, PRIORITY);
  }

  @Test
  public void testSetup() {
    assertThat(this.rank.getIdentifier(), is(equalTo(IDENTIFIER)));
    assertThat(this.rank.getName(), is(equalTo(NAME)));
    assertThat(this.rank.getPriority(), is(equalTo(PRIORITY)));
  }
}
