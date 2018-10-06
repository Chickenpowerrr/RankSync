package com.gmail.chickenpowerrr.ranksync.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerFactory<P> {

    CompletableFuture<Player> getPlayer(P internalObject);

    CompletableFuture<Player> getPlayer(UUID uuid);

    Bot<P, ?> getBot();

    CompletableFuture<Void> setUuid(String playerId, UUID uuid);
}
