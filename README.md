
# LunarEconomy

**LunarEconomy** é um plugin de economia para Minecraft desenvolvido com **Paper API**. Ele oferece suporte a moedas personalizadas (Lunar 🌙 e Solar ☀️), fácil integração com outros plugins através de uma API e eventos customizados para permitir interações e extensibilidade.

---

## Funcionalidades

- Gerenciamento de saldos para moedas `Lunar` e `Solar`.
- Comandos administrativos para modificar os saldos dos jogadores.
- Suporte a **API** para integração com outros plugins.
- Disparo de **Eventos Customizados** em mudanças de saldo.
- Suporte a **Placeholders** para integração com **PlaceholderAPI**.
- Armazenamento persistente dos saldos no arquivo `economy.yml`.

---

## Comandos e Permissões

### **Comandos**

| Comando              | Descrição                                  | Exemplo de Uso                              |
|----------------------|--------------------------------------------|--------------------------------------------|
| `/saldo`             | Mostra o saldo de moedas do jogador.       | `/saldo`                                   |
| `/economy`           | Gerencia saldos de jogadores.              | `/economy add <player> <moeda> <quantia>`  |
| `/topsaldo`          | Mostra os 5 jogadores com maiores saldos.  | `/topsaldo`                                |
| `/lunarinfo`         | Exibe informações sobre o plugin.          | `/lunarinfo`                               |

### **Subcomandos do `/economy`**

| Subcomando | Descrição                              | Exemplo de Uso                              |
|------------|----------------------------------------|--------------------------------------------|
| `add`      | Adiciona saldo a um jogador.           | `/economy add Player lunar 500`            |
| `remove`   | Remove saldo de um jogador.            | `/economy remove Player solar 200`         |
| `set`      | Define o saldo de um jogador.          | `/economy set Player lunar 1000`           |
| `balance`  | Mostra o saldo de um jogador.          | `/economy balance Player solar`            |

### **Permissões**

| Permissão               | Descrição                                  |
|-------------------------|--------------------------------------------|
| `lunareconomy.saldo`    | Permite usar o comando `/saldo`.           |
| `lunareconomy.manage`   | Permite usar o comando `/economy`.         |
| `lunareconomy.topsaldo` | Permite usar o comando `/topsaldo`.        |
| `lunareconomy.info`     | Permite usar o comando `/lunarinfo`.       |

---

## Uso da API

O **LunarEconomyAPI** oferece métodos simples para interagir com os saldos de jogadores e integrar o sistema de economia com outros plugins.

### **Inicializando a API**

```java
import org.lunar.lunarEconomy.LunarEconomyAPI;

public class MeuPlugin {
    @Override
    public void onEnable() {
        // Verifica se o LunarEconomy está instalado
        if (getServer().getPluginManager().getPlugin("LunarEconomy") == null) {
            getLogger().severe("LunarEconomy não encontrado!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // API disponível
        LunarEconomyAPI api = LunarEconomyAPI.getInstance();
    }
}
```

### **Exemplo de Uso da API**

```java
// Obtendo o saldo de um jogador
double saldoLunar = api.getBalance(player.getUniqueId(), "lunar");
double saldoSolar = api.getBalance(player.getUniqueId(), "solar");

// Modificando o saldo
api.addBalance(player.getUniqueId(), "lunar", 500);
api.removeBalance(player.getUniqueId(), "solar", 300);
api.setBalance(player.getUniqueId(), "lunar", 1000);
```

---

## Placeholders

O **LunarEconomy** suporta integração com o **PlaceholderAPI** para exibir saldos diretamente em mensagens, scoreboards, hologramas, etc.

### **Placeholders Disponíveis**

| Placeholder                | Descrição                                     |
|----------------------------|-----------------------------------------------|
| `%lunareconomy_saldo_lunar%` | Retorna o saldo de Lunar do jogador.         |
| `%lunareconomy_saldo_solar%` | Retorna o saldo de Solar do jogador.         |

### **Exemplo de Uso**

1. Instale o [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).
2. Use os placeholders nos plugins que suportam PlaceholderAPI, como scoreboards ou hologramas:
    - **Exemplo:**
        - `%lunareconomy_saldo_lunar%`: Exibe o saldo Lunar.
        - `%lunareconomy_saldo_solar%`: Exibe o saldo Solar.

---

## Eventos Customizados

O plugin dispara eventos customizados que você pode escutar para reagir a alterações nos saldos.

### **Eventos Disponíveis**

| Evento                 | Descrição                                  |
|------------------------|--------------------------------------------|
| `BalanceChangeEvent`   | Disparado quando o saldo de um jogador é alterado.|

#### **BalanceChangeEvent**

Este evento é disparado sempre que o saldo de um jogador é modificado. Ele contém as seguintes informações:

| Método           | Retorno    | Descrição                              |
|-------------------|------------|----------------------------------------|
| `getPlayerId()`   | `UUID`     | Retorna o UUID do jogador.             |
| `getCurrency()`   | `String`   | Retorna a moeda afetada (Lunar ou Solar).|
| `getOldBalance()` | `double`   | Retorna o saldo antes da alteração.    |
| `getNewBalance()` | `double`   | Retorna o saldo após a alteração.      |

#### **Exemplo de Listener**

```java
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.lunar.lunarEconomy.events.BalanceChangeEvent;

public class BalanceListener implements Listener {

    @EventHandler
    public void onBalanceChange(BalanceChangeEvent event) {
        System.out.println("Saldo alterado!");
        System.out.println("Jogador: " + event.getPlayerId());
        System.out.println("Moeda: " + event.getCurrency());
        System.out.println("Antes: " + event.getOldBalance());
        System.out.println("Depois: " + event.getNewBalance());
    }
}
```

---

## Arquivo de Configuração

O arquivo `economy.yml` é usado para armazenar os saldos dos jogadores de forma persistente. Ele é gerado automaticamente na pasta do plugin.

---
