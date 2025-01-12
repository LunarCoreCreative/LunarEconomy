package org.lunar.lunarEconomy;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EconomyPlaceholder extends PlaceholderExpansion {

    private final LunarEconomy plugin;

    public EconomyPlaceholder(LunarEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lunareconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SeuNome";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        return switch (params.toLowerCase()) {
            case "saldo_lunar" -> String.valueOf(LunarEconomyAPI.getInstance().getBalance(player.getUniqueId(), "lunar"));
            case "saldo_solar" -> String.valueOf(LunarEconomyAPI.getInstance().getBalance(player.getUniqueId(), "solar"));
            default -> null;
        };
    }
}
