package org.lunar.lunarEconomy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lunar.lunarEconomy.events.BalanceChangeEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EconomyManager {

    public static final String LUNAR_ICON = "₪"; // Ícone da moeda Lunar
    public static final String SOLAR_ICON = "☀"; // Ícone da moeda Solar

    private final File file;
    private final FileConfiguration config;
    private final JavaPlugin plugin;

    public EconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Não foi possível criar o diretório do plugin!");
        }

        file = new File(dataFolder, "economy.yml");
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    plugin.getLogger().info("Arquivo economy.yml criado com sucesso!");
                } else {
                    plugin.getLogger().warning("Arquivo economy.yml já existia.");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Erro ao criar o arquivo economy.yml: " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Retorna o saldo de um jogador para uma moeda.
     *
     * @param playerId UUID do jogador
     * @param currency Nome da moeda
     * @return Saldo atual
     */
    public double getBalance(UUID playerId, String currency) {
        validateCurrency(currency);
        return config.getDouble(playerId + "." + currency, 0.0);
    }

    /**
     * Define o saldo de um jogador para uma moeda.
     *
     * @param playerId UUID do jogador
     * @param currency Nome da moeda
     * @param amount   Novo saldo
     */
    public void setBalance(UUID playerId, String currency, double amount) {
        validateCurrency(currency);
        double oldBalance = getBalance(playerId, currency);
        config.set(playerId + "." + currency, amount);
        save();

        // Disparar o evento BalanceChangeEvent
        plugin.getServer().getPluginManager().callEvent(
                new BalanceChangeEvent(playerId, currency, oldBalance, amount)
        );
    }

    /**
     * Adiciona um valor ao saldo de um jogador para uma moeda.
     *
     * @param playerId UUID do jogador
     * @param currency Nome da moeda
     * @param amount   Valor a ser adicionado
     */
    public void addBalance(UUID playerId, String currency, double amount) {
        double currentBalance = getBalance(playerId, currency);
        setBalance(playerId, currency, currentBalance + amount);
    }

    /**
     * Remove um valor do saldo de um jogador para uma moeda.
     *
     * @param playerId UUID do jogador
     * @param currency Nome da moeda
     * @param amount   Valor a ser removido
     */
    public void removeBalance(UUID playerId, String currency, double amount) {
        double currentBalance = getBalance(playerId, currency);
        setBalance(playerId, currency, Math.max(0, currentBalance - amount));
    }

    /**
     * Obtém o top jogadores com base no saldo de uma moeda.
     *
     * @param currency Nome da moeda
     * @param limit    Número máximo de jogadores no ranking
     * @return Lista ordenada de jogadores e seus saldos
     */
    public List<Map.Entry<UUID, Double>> getTopBalances(String currency, int limit) {
        validateCurrency(currency);

        Map<UUID, Double> balances = new HashMap<>();
        for (String key : config.getKeys(false)) {
            try {
                UUID playerId = UUID.fromString(key);
                double balance = config.getDouble(key + "." + currency, 0.0);
                balances.put(playerId, balance);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Chave inválida no arquivo de configuração: " + key);
            }
        }

        return balances.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Salva o arquivo de configuração no disco.
     */
    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao salvar o arquivo economy.yml: " + e.getMessage());
        }
    }

    /**
     * Valida se a moeda especificada é suportada.
     *
     * @param currency Nome da moeda
     */
    private void validateCurrency(String currency) {
        if (!currency.equalsIgnoreCase("lunar") && !currency.equalsIgnoreCase("solar")) {
            throw new IllegalArgumentException("Moeda inválida: " + currency);
        }
    }
}
