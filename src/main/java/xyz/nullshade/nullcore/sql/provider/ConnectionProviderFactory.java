package xyz.nullshade.nullcore.sql.provider;

import com.google.inject.Inject;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ConnectionProviderFactory {

    private final JavaPlugin javaPlugin;

    @Inject
    public ConnectionProviderFactory(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public ConnectionProvider createProvider(ConfigurationSection config) {
       return new MySQLConnectionProvider(javaPlugin, config);
    }
}

