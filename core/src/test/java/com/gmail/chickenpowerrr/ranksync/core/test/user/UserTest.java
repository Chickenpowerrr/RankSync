package com.gmail.chickenpowerrr.ranksync.core.test.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.ArrayList;
import java.util.Date;
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
public class UserTest {

  private Date startDate;
  private Date endDate;
  private User user;

  @Mock
  private Account account1, account2;

  @BeforeEach
  public void setUp() {
    this.startDate = new Date();
    this.startDate.setTime(System.currentTimeMillis() - 2000);

    this.endDate = new Date();
    this.endDate.setTime(System.currentTimeMillis() - 1000);

    this.user = new User(new ArrayList<>());
  }

  @Test
  public void testSetup() {
    assertThat(this.user.getAccounts(), hasSize(0));
  }

  @Test
  public void testAddLinkNewAccount() {
    UserLink userLink = new UserLink(this.account1, this.user, this.startDate);
    assertThat(this.user.getAccounts(), hasSize(0));
    
    assertThat(this.user.addLink(userLink), is(true));

    assertThat(this.user.getAccounts(), hasSize(1));
    assertThat(this.user.getAccounts(), hasItem(userLink.getAccount()));
  }

  @Test
  public void testAddLinkDuplicateAccount() {
    UserLink userLink1 = new UserLink(this.account1, this.user, this.startDate);
    UserLink userLink2 = new UserLink(this.account1, this.user, this.startDate);

    assertThat(this.user.getAccounts(), hasSize(0));

    assertThat(this.user.addLink(userLink1), is(true));
    assertThat(this.user.addLink(userLink2), is(false));

    assertThat(this.user.getAccounts(), hasSize(1));
    assertThat(this.user.getAccounts(), hasItem(userLink1.getAccount()));
  }

  @Test
  public void testAddLinkMultipleAccounts() {
    UserLink userLink1 = new UserLink(this.account1, this.user, this.startDate);
    UserLink userLink2 = new UserLink(this.account2, this.user, this.startDate);

    assertThat(this.user.getAccounts(), hasSize(0));

    assertThat(this.user.addLink(userLink1), is(true));
    assertThat(this.user.addLink(userLink2), is(true));

    assertThat(this.user.getAccounts(), hasSize(2));
    assertThat(this.user.getAccounts(), hasItem(userLink1.getAccount()));
    assertThat(this.user.getAccounts(), hasItem(userLink2.getAccount()));
  }

  @Test
  public void testGetAccountsExpired() {
    UserLink userLink = new UserLink(this.account1, this.user, this.startDate, this.endDate);

    assertThat(this.user.getAccounts(), hasSize(0));

    assertThat(this.user.addLink(userLink), is(false));

    assertThat(this.user.getAccounts(), hasSize(0));
  }

  @Test
  public void testGetAccountsOverridden() {
    UserLink userLinkExpired = new UserLink(this.account1, this.user, this.startDate, this.endDate);
    UserLink userLinkValid = new UserLink(this.account1, this.user, this.endDate);

    assertThat(this.user.getAccounts(), hasSize(0));

    System.out.println(userLinkExpired.isActive());
    assertThat(this.user.addLink(userLinkExpired), is(false));
    assertThat(this.user.addLink(userLinkValid), is(true));

    assertThat(this.user.getAccounts(), hasSize(1));
    assertThat(this.user.getAccounts(), hasItem(userLinkValid.getAccount()));
  }
}
