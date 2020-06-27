package com.gmail.chickenpowerrr.ranksync.core.test.user;

import static com.gmail.chickenpowerrr.ranksync.core.util.Util.cast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gmail.chickenpowerrr.ranksync.core.link.LinkManager;
import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankLink;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import com.gmail.chickenpowerrr.ranksync.core.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
  private RankLink rankLink;

  @Mock
  private LinkManager linkManager;

  @Mock
  private Reward<TestPlatform> reward;

  @Mock
  private Account<TestPlatform> account1, account2;

  @Mock
  private Account<?> linkedAccount;

  @Mock
  private Platform<?> platform, linkedPlatform;

  @Mock
  private Rank<?> rank1, rank2;

  @Mock
  private Rank<TestPlatform> rank3, rank4;

  @BeforeEach
  public void setUp() {
    this.startDate = new Date();
    this.startDate.setTime(System.currentTimeMillis() - 2000);

    this.endDate = new Date();
    this.endDate.setTime(System.currentTimeMillis() - 1000);

    this.rewards = new HashSet<>();
    this.rewards.add(this.reward);
    this.user = new User(this.linkManager, new ArrayList<>());
    this.rankLink = new RankLink("hey", "%name%",
        Util.mapOf(this.platform, Arrays.asList(this.rank3, this.rank4),
            this.linkedPlatform, Arrays.asList(this.rank1, this.rank2)));

    when(this.linkedAccount.getPlatform()).thenReturn(cast(this.linkedPlatform));
    when(this.linkManager.getRankLinks()).thenReturn(Collections.singleton(this.rankLink));
    when(this.linkedPlatform.getRanks(any())).thenAnswer(invocation -> {
      CompletableFuture<List<Rank<?>>> completableFuture = new CompletableFuture<>();
      completableFuture.complete(Arrays.asList(this.rank1, this.rank2));
      return completableFuture;
    });
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

  @Test
  public void testUpdate() {
    this.user.addLink(new UserLink<>(this.account1, this.user), this.rewards);
    this.user.addLink(new UserLink<>(this.linkedAccount, this.user), new HashSet<>());

    assertThat(this.user.getAccounts()).hasSize(2);

    this.user.update();

    assertThat(this.user.getAccounts()).hasSize(2);
    verify(this.account1, atLeastOnce()).updateRanks(eq(Arrays.asList(this.rank3, this.rank4)));
  }
}
