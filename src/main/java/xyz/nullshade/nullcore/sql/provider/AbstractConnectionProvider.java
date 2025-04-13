package xyz.nullshade.nullcore.sql.provider;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractConnectionProvider implements ConnectionProvider{
    protected final JavaPlugin plugin;
    protected final ConfigurationSection config;

    public AbstractConnectionProvider(JavaPlugin plugin, ConfigurationSection config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public void initialize() {
        // In Unterklassen implementieren
    }

    @Override
    public void closeAll() {
        // In Unterklassen implementieren
    }
}
