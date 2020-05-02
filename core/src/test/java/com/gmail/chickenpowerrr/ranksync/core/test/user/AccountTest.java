package com.gmail.chickenpowerrr.ranksync.core.test.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.chickenpowerrr.ranksync.core.test.util.TestPlatform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

  private static final String IDENTIFIER = "identifier";
  private static final List<UserLink<TestPlatform>> LINKS = Collections.emptyList();

  private TestPlatform testPlatform;
  private Account<TestPlatform> account;

  @BeforeEach
  public void setUp() {
    this.testPlatform = new TestPlatform();

    this.account = new Account<TestPlatform>(IDENTIFIER, this.testPlatform, LINKS) {
      @Override
      public @NotNull String getName() {
        return "SomeName";
      }

      @Override
      public boolean updateName(@NotNull String name) {
        return false;
      }

      @Override
      public @NotNull CompletableFuture<String> formatName(@NotNull Account<TestPlatform> account) {
        return new CompletableFuture<>();
      }
    };
  }

  @Test
  public void testSetup() {
    assertThat(this.account.getIdentifier()).isEqualTo(IDENTIFIER);
  }
}
