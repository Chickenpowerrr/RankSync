package com.gmail.chickenpowerrr.ranksync.spigot.language;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import lombok.Setter;
import org.bukkit.ChatColor;

public enum Translation {

    STARTUP_TRANSLATIONS,
    STARTUP_RANKS,
    INVALID_CODE,
    RANKSYNC_COMMAND_USAGE,
    RANKSYNC_COMMAND_REQUEST_LIMIT,
    RANKSYNC_COMMAND_LINKED,
    RANKSYNC_COMMAND_ALREADY_LINKED,
    RANKSYNC_COMMAND_GET_CODE,
    COMMAND_PLAYERONLY,
    UNSYNC_COMMAND_UNLINKED,
    UNSYNC_COMMAND_NOT_LINKED,
    UNSYNC_COMMAND_INVALID_SERVICE,
    UNSYNC_COMMAND_USAGE,
    DISCORD_LINKINFO;

    @Setter private static LanguageHelper languageHelper;
    @Setter private static String language;

    private final String key;

    Translation() {
        this.key = "minecraft-" + super.toString().toLowerCase().replace("_", "-");
    }

    Translation(String key) {
        this.key = key;
    }

    public String getTranslation(String... replacements) {
        String message = languageHelper.getMessage(language, this.key);

        for(int i = 0; i < replacements.length; i += 2) {
            message = message.replaceAll("%" + replacements[i] + "%", replacements[i + 1]);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public String toString() {
        return key;
    }
}
