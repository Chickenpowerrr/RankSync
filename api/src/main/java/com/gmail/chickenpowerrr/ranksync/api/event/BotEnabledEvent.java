package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BotEnabledEvent implements Event {

    @Getter private final Bot bot;
}
