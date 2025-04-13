package xyz.nullshade.nullcore.sql.builders;

import xyz.nullshade.nullcore.sql.provider.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractQueryBuilder<T extends AbstractQueryBuilder<T>> extends AbstractSQLBuilder<T> implements QueryableStatement<T> {
    @Override
    public <R> R executeQuery(Connection connection, Function<ResultSet, R> handler) throws SQLException {
        try (PreparedStatement stmt = prepareStatement(connection);
             ResultSet rs = stmt.executeQuery()) {
            return handler.apply(rs);
        }
    }

    @Override
    public <R> CompletableFuture<R> executeQueryAsync(ConnectionProvider provider, Function<ResultSet, R> handler) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = provider.connection()) {
                return executeQuery(conn, handler);
            } catch (SQLException e) {
                throw new RuntimeException("SQL query execution failed", e);
            }
        });
    }

    @Override
    public void executeQueryForEach(Connection connection, Consumer<ResultSet> rowConsumer) throws SQLException {
        try (PreparedStatement stmt = prepareStatement(connection);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rowConsumer.accept(rs);
            }
        }
    }
}

