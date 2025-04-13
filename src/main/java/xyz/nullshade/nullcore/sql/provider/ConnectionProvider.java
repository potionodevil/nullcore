package xyz.nullshade.nullcore.sql.provider;

import xyz.nullshade.nullcore.sql.SQL;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

    Connection connection() throws SQLException;

    void closeAll();

    void initialize();


}
