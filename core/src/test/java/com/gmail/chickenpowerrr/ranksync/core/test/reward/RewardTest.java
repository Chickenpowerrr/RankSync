package com.gmail.chickenpowerrr.ranksync.core.test.reward;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import org.jetbrains.annotations.NotNull;
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
public class RewardTest {

  private static final String IDENTIFIER = "identifier";
  private static final int MAX_ISSUES = 5;

  private Reward reward;

  @Mock
  private Platform platform;

  @BeforeEach
  public void setUp() {
    this.reward = new Reward(this.platform, IDENTIFIER, MAX_ISSUES) {
      @Override
      public boolean apply(@NotNull Account account) {
        return true;
      }
    };
  }

  @Test
  public void testSetup() {
    assertThat(this.reward.getMaxIssues(), is(equalTo(MAX_ISSUES)));
    assertThat(this.reward.getIdentifier(), is(equalTo(IDENTIFIER)));
  }
}
