package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class RankLinkTest {

  private static final String RANK_IDENTIFIER = "identifier";
  private static final String RANK_NAME = "name";
  private static final String NAME_FORMAT = "%name%";
  private static final int SOURCE_PRIORITY = 1;
  private static final int DESTINATION_PRIORITY = 2;

  private Rank source;
  private Rank destination;
  private RankLink rankLink;

  @BeforeEach
  public void setUp() {
    this.source = new Rank(RANK_IDENTIFIER + 1, RANK_NAME + 1, SOURCE_PRIORITY);
    this.destination = new Rank(RANK_IDENTIFIER + 2, RANK_NAME + 2, DESTINATION_PRIORITY);
    this.rankLink = new RankLink(NAME_FORMAT, this.source, this.destination);
  }

  @Test
  public void testSetUp() {
    assertThat(this.rankLink.getPriority(), is(equalTo(DESTINATION_PRIORITY)));
    assertThat(this.rankLink.getSource(), is(equalTo(this.source)));
    assertThat(this.rankLink.getDestination(), is(equalTo(this.destination)));
    assertThat(this.rankLink.getNameFormat(), is(equalTo(NAME_FORMAT)));
  }
}
