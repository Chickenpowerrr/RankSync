package com.gmail.chickenpowerrr.ranksync.spigot.link;

import org.bukkit.command.CommandSender;

import java.util.UUID;

abstract class AbstractMiddleware {

  private AbstractMiddleware next;
  protected final LinkHelper linkHelper;

  public AbstractMiddleware(LinkHelper linkHelper) {
    this.linkHelper = linkHelper;
  }

  public AbstractMiddleware setNext(AbstractMiddleware next) {
    this.next = next;
    return next;
  }

  final boolean allowed(CommandSender commandSender, UUID uuid, String service, String key) {
    if (next == null) {
      return check(commandSender, uuid, service, key);
    } else {
      return check(commandSender, uuid, service, key) && this.next
          .allowed(commandSender, uuid, service, key);
    }
  }

  protected abstract boolean check(CommandSender commandSender, UUID uuid, String service,
      String key);
}
