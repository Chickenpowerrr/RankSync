package com.gmail.chickenpowerrr.ranksync.server.link;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Link implements com.gmail.chickenpowerrr.ranksync.api.link.Link {

  private final List<String> minecraftRanks;
  private final List<String> platformRanks;
  private final String nameFormat;
  private final Bot<?, ?> bot;
}
