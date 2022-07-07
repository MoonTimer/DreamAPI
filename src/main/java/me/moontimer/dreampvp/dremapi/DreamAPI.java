package me.moontimer.dreampvp.dremapi;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public final class DreamAPI extends Plugin {

    private static DreamAPI instance;
    private MySQL mySQL;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        mySQL = new MySQL("localhost", "test", "test", "test", "3306");
        mySQL.connect();
        this.getProxy().getScheduler().schedule(this, new Runnable() {
            public void run() {
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                    MySQLPlayerManager.updateTime(all.getUniqueId());
            }
        },  0L, 1L, TimeUnit.MINUTES);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static DreamAPI getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
