package com.gmail.chickenpowerrr.ranksync.api;

public interface CommandFactory {

    Command getCommand(String label);

    void addCommand(Command command);
}
