package com.gmail.chickenpowerrr.ranksync.api;

public interface DatabaseFactory {

    Database getDatabase(String name, Properties credentials);

    Bot getBot();
}
