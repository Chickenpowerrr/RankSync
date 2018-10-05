package com.gmail.chickenpowerrr.ranksync.api;

import java.util.Collection;
import java.util.List;

public interface Command {

    String getLabel();

    Collection<String> getAliases();

    boolean hasPermission(Player player);

    String execute(Player invoker, List<String> arguments);
}
