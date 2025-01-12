package org.lunar.lunarEconomy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LunarEconomy extends JavaPlugin {

    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        this.economyManager = new EconomyManager(this);

        // Inicializar a API
        new LunarEconomyAPI(economyManager);

        // Registrar PlaceholderAPI, se disponível
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EconomyPlaceholder(this).register();
            getLogger().info("PlaceholderAPI encontrado! Placeholders ativados.");
        } else {
            getLogger().info("PlaceholderAPI não encontrado. Placeholders estão desativados.");
        }

        // Registrar o Tab Completer
        if (getCommand("economy") != null) {
            getCommand("economy").setTabCompleter(new EconomyTabCompleter());
        } else {
            getLogger().warning("O comando 'economy' não está registrado no plugin.yml!");
        }

        if (getCommand("topsaldo") == null) {
            getLogger().warning("O comando 'topsaldo' não está registrado no plugin.yml!");
        }

        getLogger().info("LunarEconomy foi ativado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LunarEconomy foi desativado!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("lunarinfo")) {
            sender.sendMessage("§a[LunarEconomy] Este plugin gerencia economias baseadas em Lunar 🌙 e Solar ☀️.");
            sender.sendMessage("§aDesenvolvido para personalizar sistemas econômicos no Minecraft!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("saldo")) {
            if (sender instanceof Player player) {
                double lunarBalance = economyManager.getBalance(player.getUniqueId(), "lunar");
                double solarBalance = economyManager.getBalance(player.getUniqueId(), "solar");

                player.sendMessage("§aSeu saldo:");
                player.sendMessage("§f" + EconomyManager.LUNAR_ICON + " Lunar: §e" + NumberFormatter.format(lunarBalance));
                player.sendMessage("§f" + EconomyManager.SOLAR_ICON + " Solar: §e" + NumberFormatter.format(solarBalance));
            } else {
                sender.sendMessage("§cApenas jogadores podem usar este comando.");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("economy")) {
            if (args.length < 4) {
                sender.sendMessage("§cUso correto: /economy <add|remove|balance|set> <player> <lunar|solar> [quantia]");
                return true;
            }

            String action = args[0];
            Player target = Bukkit.getPlayer(args[1]);
            String currency = args[2].toLowerCase();
            String icon = currency.equals("lunar") ? EconomyManager.LUNAR_ICON : EconomyManager.SOLAR_ICON;

            if (target != null) {
                try {
                    double amount = Double.parseDouble(args[3]);
                    switch (action.toLowerCase()) {
                        case "add" -> {
                            economyManager.addBalance(target.getUniqueId(), currency, amount);
                            sender.sendMessage("§aAdicionado §f" + NumberFormatter.format(amount) + " " + icon + " §apara §f" + target.getName() + ".");
                        }
                        case "remove" -> {
                            economyManager.removeBalance(target.getUniqueId(), currency, amount);
                            sender.sendMessage("§aRemovido §f" + NumberFormatter.format(amount) + " " + icon + " §de §f" + target.getName() + ".");
                        }
                        case "set" -> {
                            economyManager.setBalance(target.getUniqueId(), currency, amount);
                            sender.sendMessage("§aSaldo definido para §f" + NumberFormatter.format(amount) + " " + icon + " §para §f" + target.getName() + ".");
                        }
                        case "balance" -> {
                            double balance = economyManager.getBalance(target.getUniqueId(), currency);
                            sender.sendMessage("§f" + target.getName() + " §apossui §f" + NumberFormatter.format(balance) + " " + icon + ".");
                        }
                        default -> sender.sendMessage("§cAção inválida! Use: add, remove, set ou balance.");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cO valor fornecido não é um número válido.");
                }
            } else {
                sender.sendMessage("§cJogador não encontrado.");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("topsaldo")) {
            sender.sendMessage("§aTop 5 jogadores por saldo:");

            // Top 5 Lunar
            sender.sendMessage("§f" + EconomyManager.LUNAR_ICON + " §eLunar:");
            List<Map.Entry<UUID, Double>> topLunar = economyManager.getTopBalances("lunar", 5);
            for (int i = 0; i < topLunar.size(); i++) {
                Map.Entry<UUID, Double> entry = topLunar.get(i);
                String playerName = Bukkit.getOfflinePlayer(entry.getKey()).getName();
                sender.sendMessage("  §f" + (i + 1) + ". §e" + playerName + ": §f" + NumberFormatter.format(entry.getValue()));
            }

            // Top 5 Solar
            sender.sendMessage("§f" + EconomyManager.SOLAR_ICON + " §eSolar:");
            List<Map.Entry<UUID, Double>> topSolar = economyManager.getTopBalances("solar", 5);
            for (int i = 0; i < topSolar.size(); i++) {
                Map.Entry<UUID, Double> entry = topSolar.get(i);
                String playerName = Bukkit.getOfflinePlayer(entry.getKey()).getName();
                sender.sendMessage("  §f" + (i + 1) + ". §e" + playerName + ": §f" + NumberFormatter.format(entry.getValue()));
            }
            return true;
        }

        return false;
    }
}
