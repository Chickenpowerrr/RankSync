package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.api.Command;
import com.gmail.chickenpowerrr.ranksync.api.Player;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkCodeCreateEvent;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class LinkCommand implements Command {

    private static final Random random = new Random();

    @Getter private final String label;
    private final Collection<String> aliases;

    private final Map<String, Long> timeOuts = new HashMap<>();

    LinkCommand(String label, Collection<String> aliases) {
        this.label = label;
        this.aliases = aliases;

        new Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Collection<String> toRemove = timeOuts.entrySet().stream().filter(entry -> entry.getValue() + 1000 * 60 * 5 < System.currentTimeMillis()).map(Map.Entry::getKey).collect(Collectors.toSet());
                toRemove.forEach(timeOuts::remove);
            }
        }, 1000 * 10, 1000 * 10);
    }

    @Override
    public String execute(Player invoker, List<String> arguments) {
        if(invoker.getUuid() == null) {
            if(!onCooldown(invoker.getPersonalId())) {
                this.timeOuts.put(invoker.getPersonalId(), System.currentTimeMillis());
                String secretKey = randomString(10 + random.nextInt(2));
                RankSyncApi.getApi().execute(new PlayerLinkCodeCreateEvent(invoker, secretKey));
                invoker.sendPrivateMessage("Hello " + invoker.getFancyName() + " and thanks for linking your Minecraft account! \n" +
                        "Your private key is: `" + secretKey + "` please DON'T share this code with anyone.\n" +
                        "To link your account please enter: `/ranksync Discord " + secretKey + "` into Minecraft.\n" +
                        "This will automatically give you the right Discord ranks. \n\n" +
                        "Have a nice day :D");
                return "Thanks " + invoker.getFancyName() + " you'll find the secret code in your private messages. DON'T share this code!";
            } else {
                invoker.sendPrivateMessage("Your message is right there ^");
                return "You are only allowed to do this every two minutes " + invoker.getFancyName() + "!";
            }
        } else {
            return "Your account has already been linked " + invoker.getFancyName();
        }
    }

    private boolean onCooldown(String identifier) {
        return this.timeOuts.containsKey(identifier) && this.timeOuts.get(identifier) + 1000 * 60 * 5 >= System.currentTimeMillis();
    }

    @Override
    public boolean hasPermission(Player player) {
        return true;
    }

    @Override
    public Collection<String> getAliases() {
        return Collections.unmodifiableCollection(this.aliases);
    }

    private String randomString(int size) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < size; i++) {
            stringBuilder.append(randomChar());
        }
        return stringBuilder.toString();
    }

    private char randomChar() {
        int randomNumber = LinkCommand.random.nextInt(52);
        char base = (randomNumber < 26) ? 'A' : 'a';
        return (char) (base + randomNumber % 26);
    }
}
