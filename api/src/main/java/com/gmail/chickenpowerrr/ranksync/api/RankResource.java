package com.gmail.chickenpowerrr.ranksync.api;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface RankResource {

    CompletableFuture<Collection<Rank>> getRanks(UUID uuid);

    void setBot(Bot bot);

    boolean isValidRank(String name);
}
