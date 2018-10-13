package com.gmail.chickenpowerrr.ranksync.api;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Database {

    CompletableFuture<UUID> getUuid(String playerId);

    CompletableFuture<Void> setUuid(String playerId, UUID uuid);

    CompletableFuture<String> getPlayerId(UUID uuid);

    CompletableFuture<Collection<Rank>> getRanks(UUID uuid);

    CompletableFuture<Boolean> isValidRank(String rankName);
}
