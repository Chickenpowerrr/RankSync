package com.gmail.chickenpowerrr.ranksync.discord.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import com.gmail.chickenpowerrr.ranksync.discord.DiscordPlatform;
import com.gmail.chickenpowerrr.ranksync.discord.DiscordRank;
import com.gmail.chickenpowerrr.ranksync.discord.RoleRankResource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class RoleRankResourceTest {

  private static final String ROLE_ID_1 = "1";
  private static final String ROLE_ID_2 = "2";
  private static final String ROLE_NAME_1 = "name1";
  private static final String ROLE_NAME_2 = "name2";
  private static final String MEMBER_ID = "5728";

  private List<Rank<DiscordPlatform>> ranks;
  private RoleRankResource rankResource;
  private List<Role> generalRoles;
  private List<Role> userRoles;

  @Mock
  private Guild guild;

  @Mock
  private Role role1, role2;

  @Mock
  private Member member;

  @Mock
  private Account<DiscordPlatform> account;

  @BeforeEach
  public void setUp() {
    this.generalRoles = new ArrayList<>();
    this.userRoles = new ArrayList<>();
    this.ranks = new ArrayList<>();

    when(this.role1.getId()).thenReturn(ROLE_ID_1);
    when(this.role2.getId()).thenReturn(ROLE_ID_2);
    when(this.role1.getName()).thenReturn(ROLE_NAME_1);
    when(this.role2.getName()).thenReturn(ROLE_NAME_2);

    this.generalRoles.add(this.role1);
    this.generalRoles.add(this.role2);
    this.userRoles.add(this.role2);
    this.ranks.add(new DiscordRank(this.role1, RoleRankResource.ROLE_TYPE, 1));
    this.ranks.add(new DiscordRank(this.role2, RoleRankResource.ROLE_TYPE, 2));

    this.rankResource = new RoleRankResource(this.guild, this.ranks);
  }

  @Test
  public void testGeneralRanks() throws Exception {
    when(this.guild.getRoleById(ROLE_ID_1)).thenReturn(this.role1);
    when(this.guild.getRoleById(ROLE_ID_2)).thenReturn(this.role2);

    Collection<Rank<DiscordPlatform>> discordRanks = this.rankResource.getRanks().get();
    assertThat(discordRanks).hasSize(this.generalRoles.size());
    assertThat(discordRanks)
        .anySatisfy(rank -> assertThat(((DiscordRank) rank).getRole()).isEqualTo(this.role1));
    assertThat(discordRanks)
        .anySatisfy(rank -> assertThat(((DiscordRank) rank).getRole()).isEqualTo(this.role2));
  }

  @Test
  public void testSpecificRanks() throws Exception {
    when(this.member.getRoles()).thenReturn(this.userRoles);
    when(this.guild.getMemberById(MEMBER_ID)).thenReturn(this.member);
    when(this.account.getIdentifier()).thenReturn(MEMBER_ID);

    Collection<Rank<DiscordPlatform>> discordRanks = this.rankResource.getRanks(this.account).get();
    assertThat(discordRanks).hasSize(this.userRoles.size());
    assertThat(discordRanks)
        .anySatisfy(rank -> assertThat(((DiscordRank) rank).getRole()).isEqualTo(this.role2));
  }
}
