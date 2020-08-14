package com.gmail.chickenpowerrr.ranksync.discord.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gmail.chickenpowerrr.ranksync.core.link.LinkManager;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import com.gmail.chickenpowerrr.ranksync.core.user.UserLink;
import com.gmail.chickenpowerrr.ranksync.core.util.Util;
import com.gmail.chickenpowerrr.ranksync.discord.DiscordAccount;
import com.gmail.chickenpowerrr.ranksync.discord.DiscordPlatform;
import com.gmail.chickenpowerrr.ranksync.discord.DiscordRank;
import com.gmail.chickenpowerrr.ranksync.discord.RoleRankResource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DiscordAccountTest {

  private static final String TYPE = "test type";
  private static final String MEMBER_NAME = "name";
  private static final String PLATFORM_FORMAT = "Platform: %name%";
  private static final String ROLE_ID_1 = "1";
  private static final String ROLE_ID_2 = "2";
  private static final String ROLE_ID_3 = "3";

  private User user;
  private DiscordAccount account;
  private DiscordPlatform discordPlatform;
  private UserLink<DiscordPlatform> userLink;
  private DiscordRank rank1, rank2, rank3;

  @Mock
  private RoleRankResource rankResource;

  @Mock
  private Member member;

  @Mock
  private Guild guild;

  @Mock
  private Role role1, role2, role3;

  @Mock
  private LinkManager linkManager;

  @BeforeEach
  public void setUp() {
    this.discordPlatform = new DiscordPlatform(PLATFORM_FORMAT, true);

    List<UserLink<DiscordPlatform>> userLinks = new ArrayList<>();
    List<Role> roles = new ArrayList<>();
    roles.add(this.role1);
    roles.add(this.role2);
    roles.add(this.role3);

    when(this.member.getId()).thenReturn("496");

    this.user = new User(this.linkManager, (List<UserLink<?>>) (List<?>) userLinks);
    this.account = new DiscordAccount(this.member, this.discordPlatform, userLinks);
    this.discordPlatform.addRankResource(this.rankResource);
    this.userLink = new UserLink<>(this.account, this.user);

    userLinks.add(this.userLink);

    when(this.member.getEffectiveName()).thenReturn(MEMBER_NAME);
    when(this.member.getRoles()).thenReturn(roles);
    when(this.role1.getName()).thenReturn("Role 1");
    when(this.role1.getId()).thenReturn("1");
    when(this.role2.getName()).thenReturn("Role 2");
    when(this.role2.getId()).thenReturn("2");
    when(this.role3.getName()).thenReturn("Role 3");
    when(this.role3.getId()).thenReturn("3");

    this.rank1 = new DiscordRank(this.role1, RoleRankResource.ROLE_TYPE, 1);
    this.rank2 = new DiscordRank(this.role2, RoleRankResource.ROLE_TYPE, 2);
    this.rank3 = new DiscordRank(this.role3, RoleRankResource.ROLE_TYPE, 3);
  }

  @Test
  public void testName() {
    assertThat(this.account.getName()).isEqualTo(MEMBER_NAME);
  }

  @Test
  public void testFormat() throws Exception {
    String name = this.account.formatName().get();
    assertThat(name).isEqualTo(PLATFORM_FORMAT.replace("%name%", MEMBER_NAME));
  }

  @Test
  public void testUpdateRanks() {
    when(this.member.getGuild()).thenReturn(this.guild);
    when(this.rankResource.getRanks()).thenReturn(
        CompletableFuture.completedFuture(Util.setOf(this.rank1, this.rank2, this.rank3)));
    when(this.rankResource.getRanks(this.account)).thenReturn(
        CompletableFuture.completedFuture(Collections.emptyList()));
    when(this.guild.getRoleById(ROLE_ID_1)).thenReturn(this.role1);
    when(this.guild.getRoleById(ROLE_ID_2)).thenReturn(this.role2);
    when(this.guild.getRoleById(ROLE_ID_3)).thenReturn(this.role3);
    when(this.guild.modifyMemberRoles(eq(this.member), anyList()))
        .thenReturn(mock(AuditableRestAction.class));

    this.account.updateRanks(Arrays.asList(
        new DiscordRank(this.role1, TYPE, 1), new DiscordRank(this.role3, TYPE, 3)));

    ArgumentCaptor<List<Role>> captor = ArgumentCaptor.forClass(List.class);
    //noinspection ResultOfMethodCallIgnored
    verify(this.guild,
        times(1)).modifyMemberRoles(eq(this.member), captor.capture());

    assertThat(captor.getValue()).containsExactly(this.role1, this.role3);
  }

  @Test
  public void testUpdateName() {
    when(this.member.modifyNickname(anyString())).thenReturn(mock(AuditableRestAction.class));

    this.account.updateName("Test Name");

    //noinspection ResultOfMethodCallIgnored
    verify(this.member).modifyNickname("Test Name");
  }
}
