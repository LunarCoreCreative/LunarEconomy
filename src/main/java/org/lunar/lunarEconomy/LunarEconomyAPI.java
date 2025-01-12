package org.lunar.lunarEconomy;

import java.util.UUID;

public class LunarEconomyAPI {

    private static LunarEconomyAPI instance;
    private final EconomyManager economyManager;

    public LunarEconomyAPI(EconomyManager economyManager) {
        this.economyManager = economyManager;
        instance = this;
    }

    public static LunarEconomyAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("A API ainda não foi inicializada!");
        }
        return instance;
    }

    public double getBalance(UUID playerId, String currency) {
        return economyManager.getBalance(playerId, currency);
    }

    public void setBalance(UUID playerId, String currency, double amount) {
        economyManager.setBalance(playerId, currency, amount);
    }

    public void addBalance(UUID playerId, String currency, double amount) {
        economyManager.addBalance(playerId, currency, amount);
    }

    public void removeBalance(UUID playerId, String currency, double amount) {
        economyManager.removeBalance(playerId, currency, amount);
    }
}
