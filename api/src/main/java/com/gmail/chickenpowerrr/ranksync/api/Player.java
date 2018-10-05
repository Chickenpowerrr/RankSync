package com.gmail.chickenpowerrr.ranksync.api;

import java.util.Collection;
import java.util.UUID;

public interface Player {

    Collection<Rank> getRanks();

    void setRanks(Collection<Rank> ranks);

    UUID getUuid();

    String getPersonalId();

    Bot getBot();

    void sendPrivateMessage(String message);

    String getFancyName();

    void updateRanks();
}
