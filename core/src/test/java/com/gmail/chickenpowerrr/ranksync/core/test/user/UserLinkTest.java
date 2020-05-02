package com.gmail.chickenpowerrr.ranksync.core.test.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
public class UserLinkTest {

  private Date startDate;
  private Date endDate;

  @Mock
  private User user;

  @Mock
  private Account<TestPlatform> account1, account2;

  @BeforeEach
  public void setUp() {
    this.startDate = new Date();
    this.startDate.setTime(System.currentTimeMillis() - 2000);

    this.endDate = new Date();
    this.endDate.setTime(System.currentTimeMillis() - 1000);
  }

  @Test
  public void testSetup() {
    UserLink<TestPlatform> userLink = new UserLink<>(this.account1, this.user);
    assertThat(userLink.getUser()).isEqualTo(this.user);
    assertThat(userLink.getAccount()).isEqualTo(this.account1);
  }

  @Test
  public void testActiveInactive() {
    UserLink<TestPlatform> userLink = new UserLink<>(this.account1, this.user, this.startDate, this.endDate);
    assertThat(userLink.isActive()).isFalse();
  }

  @Test
  public void testActiveActive() {
    UserLink<TestPlatform> userLink = new UserLink<>(this.account1, this.user, this.startDate);
    assertThat(userLink.isActive()).isTrue();
  }

  @Test
  public void testUnlinkNew() {
    UserLink<TestPlatform> userLink = new UserLink<>(this.account1, this.user, this.startDate);
    Reward<TestPlatform> reward = mock(Reward.class);

    Collection<Reward<TestPlatform>> rewards = new HashSet<>();
    rewards.add(reward);

    assertThat(userLink.isActive()).isTrue();

    assertThat(userLink.unlink(rewards)).isTrue();

    assertThat(userLink.isActive()).isFalse();
    verify(reward, times(1)).apply(userLink.getAccount());
  }

  @Test
  public void testUnlinkOld() {
    UserLink<TestPlatform> userLink = new UserLink<>(this.account1, this.user, this.startDate, this.endDate);
    Reward<TestPlatform> reward = mock(Reward.class);
    Collection<Reward<TestPlatform>> rewards = new HashSet<>();
    rewards.add(reward);

    assertThat(userLink.isActive()).isFalse();

    assertThat(userLink.unlink(rewards)).isFalse();

    assertThat(userLink.isActive()).isFalse();
    verify(reward, times(0)).apply(userLink.getAccount());
  }
}
