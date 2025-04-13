package xyz.nullshade.nullcore.sql.builders;

import xyz.nullshade.nullcore.sql.provider.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface ExecutableStatement<T extends ExecutableStatement<T>> {

    int execute(Connection connection) throws SQLException;

    CompletableFuture<Integer> executeAsync(ConnectionProvider provider);

    String getSQL();

    List<Object> params();
}
