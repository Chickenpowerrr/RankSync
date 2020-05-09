package com.gmail.chickenpowerrr.ranksync.discord.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    this.generalRoles.add(this.role1);
    this.generalRoles.add(this.role2);
    this.userRoles.add(this.role2);

    when(this.guild.getRoles()).thenReturn(this.generalRoles);
    when(this.guild.getMember(any())).thenReturn(this.member);
    when(this.member.getRoles()).thenReturn(this.userRoles);

    this.rankResource = new RoleRankResource(this.guild);
  }

  @Test
  public void testGeneralRanks() throws Exception {
    Collection<Rank<DiscordPlatform>> discordRanks = this.rankResource.getRanks().get();
    assertThat(discordRanks).hasSize(this.generalRoles.size());
    assertThat(discordRanks)
        .anySatisfy(rank -> assertThat(((DiscordRank) rank).getRole()).isEqualTo(this.role1));
    assertThat(discordRanks)
        .anySatisfy(rank -> assertThat(((DiscordRank) rank).getRole()).isEqualTo(this.role2));
  }

  @Test
  public void testSpecificRanks() throws Exception {
    Collection<Rank<DiscordPlatform>> discordRanks = this.rankResource.getRanks(this.account).get();
    assertThat(discordRanks).hasSize(this.userRoles.size());
    assertThat(discordRanks)
        .anySatisfy(rank -> assertThat(((DiscordRank) rank).getRole()).isEqualTo(this.role2));
  }
}
