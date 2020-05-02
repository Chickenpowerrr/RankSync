package com.gmail.chickenpowerrr.ranksync.core.test.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.ArrayList;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserTest {

  private Date startDate;
  private Date endDate;
  private User user;
  private Collection<Reward<TestPlatform>> rewards;

  @Mock
  private Reward<TestPlatform> reward;

  @Mock
  private Account<TestPlatform> account1, account2;

  @BeforeEach
  public void setUp() {
    this.startDate = new Date();
    this.startDate.setTime(System.currentTimeMillis() - 2000);

    this.endDate = new Date();
    this.endDate.setTime(System.currentTimeMillis() - 1000);

    this.rewards = new HashSet<>();
    this.rewards.add(this.reward);
    this.user = new User(new ArrayList<>());
  }

  @Test
  public void testSetup() {
    assertThat(this.user.getAccounts()).isEmpty();
  }

  @Test
  public void testAddLinkNewAccount() {
    UserLink<TestPlatform> userLink = new UserLink<>(this.account1, this.user, this.startDate);
    assertThat(this.user.getAccounts()).isEmpty();

    assertThat(this.user.addLink(userLink, this.rewards)).isTrue();

    assertThat(this.user.getAccounts()).hasSize(1);
    assertThat(this.user.getAccounts()).contains(userLink.getAccount());
    verify(this.reward, times(1)).apply(userLink.getAccount());
  }

  @Test
  public void testAddLinkDuplicateAccount() {
    UserLink<TestPlatform> userLink1 = new UserLink<>(this.account1, this.user, this.startDate);
    UserLink<TestPlatform> userLink2 = new UserLink<>(this.account1, this.user, this.startDate);

    assertThat(this.user.getAccounts()).isEmpty();

    assertThat(this.user.addLink(userLink1, this.rewards)).isTrue();
    assertThat(this.user.addLink(userLink2, this.rewards)).isFalse();

    assertThat(this.user.getAccounts()).hasSize(1);
    assertThat(this.user.getAccounts()).contains(userLink1.getAccount());
    verify(this.reward, times(1)).apply(userLink1.getAccount());
  }

  @Test
  public void testAddLinkMultipleAccounts() {
    UserLink<TestPlatform> userLink1 = new UserLink<>(this.account1, this.user, this.startDate);
    UserLink<TestPlatform> userLink2 = new UserLink<>(this.account2, this.user, this.startDate);

    assertThat(this.user.getAccounts()).isEmpty();

    assertThat(this.user.addLink(userLink1, this.rewards)).isTrue();
    assertThat(this.user.addLink(userLink2, this.rewards)).isTrue();

    assertThat(this.user.getAccounts()).hasSize(2);
    assertThat(this.user.getAccounts()).contains(userLink1.getAccount());
    assertThat(this.user.getAccounts()).contains(userLink2.getAccount());

    verify(this.reward, times(1)).apply(userLink1.getAccount());
    verify(this.reward, times(1)).apply(userLink2.getAccount());
  }

  @Test
  public void testGetAccountsExpired() {
    UserLink<TestPlatform> userLink = new UserLink<>(this.account1, this.user, this.startDate, this.endDate);

    assertThat(this.user.getAccounts()).isEmpty();

    assertThat(this.user.addLink(userLink, this.rewards)).isFalse();

    assertThat(this.user.getAccounts()).isEmpty();
    verify(this.reward, times(0)).apply(userLink.getAccount());
  }

  @Test
  public void testGetAccountsOverridden() {
    UserLink<TestPlatform> userLinkExpired = new UserLink<>(this.account1, this.user, this.startDate, this.endDate);
    UserLink<TestPlatform> userLinkValid = new UserLink<>(this.account2, this.user, this.endDate);

    assertThat(this.user.getAccounts()).isEmpty();

    assertThat(this.user.addLink(userLinkExpired, this.rewards)).isFalse();
    assertThat(this.user.addLink(userLinkValid, this.rewards)).isTrue();

    assertThat(this.user.getAccounts()).hasSize(1);
    assertThat(this.user.getAccounts()).contains(userLinkValid.getAccount());

    verify(this.reward, times(0)).apply(userLinkExpired.getAccount());
    verify(this.reward, times(1)).apply(userLinkValid.getAccount());
  }
}
