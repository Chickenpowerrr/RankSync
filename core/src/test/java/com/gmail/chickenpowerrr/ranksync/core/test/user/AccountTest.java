package com.gmail.chickenpowerrr.ranksync.core.test.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.awt.image.ImageWatched.Link;

public class AccountTest {

  private static final String IDENTIFIER = "identifier";
  private static final List<Link> LINKS = Collections.emptyList();

  private Account account;

  @BeforeEach
  public void setUp() {
    this.account = new Account(IDENTIFIER, LINKS) {
      @Override
      public @NotNull String getName() {
        return null;
      }

      @Override
      public boolean updateName(@NotNull String name) {
        return false;
      }
    };
  }

  @Test
  public void testSetup() {
    assertThat(this.account.getIdentifier(), is(equalTo(IDENTIFIER)));
  }
}
