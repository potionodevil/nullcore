package xyz.nullshade.nullcore.sql.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class MySQLConnectionProvider extends AbstractConnectionProvider{
    private HikariDataSource dataSource;

    public MySQLConnectionProvider(JavaPlugin plugin, ConfigurationSection config) {
        super(plugin, config);
    }

    @Override
    public void initialize() {
        HikariConfig hikariConfig = new HikariConfig();

        // Basis-Konfiguration
        String host = config.getString("host", "localhost");
        int port = config.getInt("port", 3306);
        String database = config.getString("database", "minecraft");
        String username = config.getString("username", "root");
        String password = config.getString("password", "");
        boolean useSSL = config.getBoolean("useSSL", false);

        // JDBC URL aufbauen
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database +
                "?useSSL=" + useSSL +
                "&useUnicode=true&characterEncoding=utf8";

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        // Pool-Konfiguration
        hikariConfig.setMaximumPoolSize(config.getInt("poolSize", 10));
        hikariConfig.setMinimumIdle(config.getInt("minIdle", 5));
        hikariConfig.setMaxLifetime(TimeUnit.MINUTES.toMillis(config.getInt("maxLifetime", 30)));
        hikariConfig.setConnectionTimeout(TimeUnit.SECONDS.toMillis(config.getInt("connectionTimeout", 10)));
        hikariConfig.setIdleTimeout(TimeUnit.MINUTES.toMillis(config.getInt("idleTimeout", 10)));

        // Zusätzliche Optionen
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");

        try {
            dataSource = new HikariDataSource(hikariConfig);
            plugin.getLogger().info("MySQL Verbindung hergestellt: " + jdbcUrl);
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Verbinden zur MySQL-Datenbank: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Connection connection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initialize();
        }
        return dataSource.getConnection();
    }

    @Override
    public void closeAll() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.getLogger().info("MySQL Verbindungen geschlossen");
        }
    }
}
