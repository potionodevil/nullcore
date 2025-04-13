package xyz.nullshade.nullcore.sql;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nullshade.nullcore.sql.provider.ConnectionProvider;
import xyz.nullshade.nullcore.sql.provider.ConnectionProviderFactory;

public class DatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ConnectionProvider.class).asEagerSingleton();
        bind(ConnectionProviderFactory.class).asEagerSingleton();

    }

    @Provides
    @Singleton
    public ConnectionProvider provideConnectionProvider(ConnectionProviderFactory factory, JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        ConfigurationSection dbConfig = plugin.getConfig().getConfigurationSection("database");

        if (dbConfig == null) {
            dbConfig = plugin.getConfig().createSection("database");
            dbConfig.set("type", "sqlite");
            dbConfig.set("database", plugin.getName().toLowerCase());
            plugin.saveConfig();
        }
        ConnectionProvider provider = factory.createProvider(dbConfig);
        provider.initialize();
        return provider;
    }
}
