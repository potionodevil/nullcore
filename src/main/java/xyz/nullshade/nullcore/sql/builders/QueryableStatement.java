package xyz.nullshade.nullcore.sql.builders;

import xyz.nullshade.nullcore.sql.provider.ConnectionProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface QueryableStatement<T extends QueryableStatement<T>> extends ExecutableStatement<T> {

    <R> R executeQuery(Connection connection, Function<ResultSet, R> handler) throws SQLException;

    <R> CompletableFuture<R> executeQueryAsync(ConnectionProvider provider, Function<ResultSet, R> handler);

    void executeQueryForEach(Connection connection, Consumer<ResultSet> rowConsumer) throws SQLException;
}
