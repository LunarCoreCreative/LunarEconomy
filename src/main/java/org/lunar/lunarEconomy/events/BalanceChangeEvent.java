package org.lunar.lunarEconomy.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BalanceChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID playerId;
    private final String currency;
    private final double oldBalance;
    private final double newBalance;

    /**
     * Evento disparado quando o saldo de um jogador é alterado.
     *
     * @param playerId   UUID do jogador
     * @param currency   Nome da moeda
     * @param oldBalance Saldo anterior
     * @param newBalance Novo saldo
     */
    public BalanceChangeEvent(UUID playerId, String currency, double oldBalance, double newBalance) {
        this.playerId = playerId;
        this.currency = currency;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getCurrency() {
        return currency;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
