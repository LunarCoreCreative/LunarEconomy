package org.lunar.lunarEconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EconomyTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("economy")) {
            if (args.length == 1) {
                // Retorna ações disponíveis
                return Arrays.asList("add", "remove", "balance", "set");
            } else if (args.length == 2) {
                // Sugere nomes de jogadores online
                List<String> playerNames = new ArrayList<>();
                sender.getServer().getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));
                return playerNames;
            } else if (args.length == 3) {
                // Retorna moedas disponíveis
                return Arrays.asList("lunar", "solar");
            }
        }
        return null;
    }
}
