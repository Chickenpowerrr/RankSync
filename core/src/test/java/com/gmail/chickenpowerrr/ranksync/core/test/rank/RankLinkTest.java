package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankLink;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class RankLinkTest {

  private static final String RANK_IDENTIFIER = "identifier";
  private static final String RANK_NAME = "name";
  private static final String NAME_FORMAT = "%name%";
  private static final int SOURCE_PRIORITY = 1;
  private static final int DESTINATION_PRIORITY = 2;

  private Rank sourceRank;
  private Rank destinationRank;
  private RankLink rankLink;

  @Mock
  private Platform sourcePlatform, destinationPlatform, uselessPlatform;

  @BeforeEach
  public void setUp() {
    this.sourceRank = new Rank(RANK_IDENTIFIER + 1, RANK_NAME + 1, SOURCE_PRIORITY);
    this.destinationRank = new Rank(RANK_IDENTIFIER + 2, RANK_NAME + 2, DESTINATION_PRIORITY);

    Map<Platform, Collection<Rank>> ranks = new HashMap<>();
    ranks.put(this.sourcePlatform, Collections.singletonList(this.sourceRank));
    ranks.put(this.destinationPlatform, Collections.singletonList(this.destinationRank));
    ranks.put(this.uselessPlatform, new HashSet<>());

    this.rankLink = new RankLink(RANK_IDENTIFIER, NAME_FORMAT, ranks);
  }

  @Test
  public void testSetUp() {
    assertThat(this.rankLink.getIdentifier(), is(equalTo(RANK_IDENTIFIER)));
    assertThat(this.rankLink.getNameFormat(), is(equalTo(NAME_FORMAT)));
  }

  @Test
  public void testGetRanks() {
    Map<Platform, Collection<Rank>> ranks = this.rankLink.getRanks();

    assertThat(ranks, is(aMapWithSize(2)));
    assertThat(ranks, hasKey(this.sourcePlatform));
    assertThat(ranks, hasKey(this.destinationPlatform));

    assertThat(ranks.get(this.sourcePlatform), hasSize(1));
    assertThat(ranks.get(this.sourcePlatform), hasItem(this.sourceRank));
    assertThat(ranks.get(this.destinationPlatform), hasSize(1));
    assertThat(ranks.get(this.destinationPlatform), hasItem(this.destinationRank));
  }

  @Test
  public void testGetPriorityUnknownPlatform() {
    Platform platform = mock(Platform.class);
    assertThat(this.rankLink.getPriority(platform), is(-1));
  }

  @Test
  public void testGetPriorityEmptyPlatform() {
    assertThat(this.rankLink.getPriority(this.uselessPlatform), is(-1));
  }

  @Test
  public void testGetPriority() {
    assertThat(this.rankLink.getPriority(this.destinationPlatform),
        is(this.destinationRank.getPriority()));
    assertThat(this.rankLink.getPriority(this.sourcePlatform),
        is(this.sourceRank.getPriority()));
  }
}
