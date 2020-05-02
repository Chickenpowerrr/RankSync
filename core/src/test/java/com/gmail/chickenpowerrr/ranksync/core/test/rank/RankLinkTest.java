package com.gmail.chickenpowerrr.ranksync.core.test.rank;

import static org.assertj.core.api.Assertions.assertThat;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class RankLinkTest {

  private static final String RANK_IDENTIFIER = "identifier";
  private static final String RANK_NAME = "name";
  private static final String NAME_FORMAT = "%name%";
  private static final int SOURCE_PRIORITY = 1;
  private static final int DESTINATION_PRIORITY = 2;

  private Rank<?> sourceRank;
  private Rank<?> destinationRank;
  private RankLink rankLink;

  @Mock
  private Platform<?> sourcePlatform, destinationPlatform, uselessPlatform;

  @BeforeEach
  public void setUp() {
    this.sourceRank = new Rank<>(RANK_IDENTIFIER + 1, RANK_NAME + 1, SOURCE_PRIORITY);
    this.destinationRank = new Rank<>(RANK_IDENTIFIER + 2, RANK_NAME + 2, DESTINATION_PRIORITY);

    Map<Platform<?>, Collection<Rank<?>>> ranks = new HashMap<>();
    ranks.put(this.sourcePlatform, Collections.singletonList(this.sourceRank));
    ranks.put(this.destinationPlatform, Collections.singletonList(this.destinationRank));
    ranks.put(this.uselessPlatform, new HashSet<>());

    this.rankLink = new RankLink(RANK_IDENTIFIER, NAME_FORMAT, ranks);
  }

  @Test
  public void testSetUp() {
    assertThat(this.rankLink.getIdentifier()).isEqualTo(RANK_IDENTIFIER);
    assertThat(this.rankLink.getNameFormat()).isEqualTo(NAME_FORMAT);
  }

  @Test
  public void testGetRanks() {
    Map<Platform<?>, Collection<Rank<?>>> ranks = this.rankLink.getRanks();

    assertThat(ranks).hasSize(2);
    assertThat(ranks).containsKey(this.sourcePlatform);
    assertThat(ranks).containsKey(this.destinationPlatform);

    assertThat(ranks.get(this.sourcePlatform)).hasSize(1);
    assertThat(ranks.get(this.sourcePlatform)).contains(this.sourceRank);
    assertThat(ranks.get(this.destinationPlatform)).hasSize(1);
    assertThat(ranks.get(this.destinationPlatform)).contains(this.destinationRank);
  }

  @Test
  public void testGetPriorityUnknownPlatform() {
    Platform<?> platform = mock(Platform.class);
    assertThat(this.rankLink.getPriority(platform)).isEqualTo(-1);
  }

  @Test
  public void testGetPriorityEmptyPlatform() {
    assertThat(this.rankLink.getPriority(this.uselessPlatform)).isEqualTo(-1);
  }

  @Test
  public void testGetPriority() {
    assertThat(this.rankLink.getPriority(this.destinationPlatform))
        .isEqualTo(this.destinationRank.getPriority());
    assertThat(this.rankLink.getPriority(this.sourcePlatform))
        .isEqualTo(this.sourceRank.getPriority());
  }
}
