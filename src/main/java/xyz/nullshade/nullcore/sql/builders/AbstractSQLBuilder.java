package xyz.nullshade.nullcore.sql.builders;

import xyz.nullshade.nullcore.sql.provider.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractSQLBuilder<T extends AbstractSQLBuilder<T>> implements ExecutableStatement<T>{
    protected final StringBuilder sqlBuilder = new StringBuilder();
    protected final List<Object> parameters = new ArrayList<>();

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    @Override
    public int execute(Connection connection) throws SQLException {
        try (PreparedStatement stmt = prepareStatement(connection)) {
            return stmt.executeUpdate();
        }
    }

    @Override
    public CompletableFuture<Integer> executeAsync(ConnectionProvider provider) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = provider.connection()) {
                return execute(conn);
            } catch (SQLException e) {
                throw new RuntimeException("SQL execution failed", e);
            }
        });
    }

    protected PreparedStatement prepareStatement(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(getSQL());
        for (int i = 0; i < parameters.size(); i++) {
            stmt.setObject(i + 1, parameters.get(i));
        }
        return stmt;
    }

    @Override
    public String getSQL() {
        return sqlBuilder.toString();
    }

    @Override
    public List<Object> params() {
        return new ArrayList<>(parameters);
    }
}

