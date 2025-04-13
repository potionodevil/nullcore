package xyz.nullshade.nullcore.sql.builders;

import java.util.ArrayList;
import java.util.List;

public class CreateTableBuilder extends AbstractSQLBuilder<CreateTableBuilder> {

    private final List<String> columns = new ArrayList<>();
    private boolean ifNotExists = false;

    public CreateTableBuilder(String tableName) {
        sqlBuilder.append("CREATE TABLE ");
        this.tableName = tableName;
    }

    private final String tableName;

    public CreateTableBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    public CreateTableBuilder column(String name, String type, boolean notNull, boolean primaryKey) {
        StringBuilder columnDef = new StringBuilder(name).append(" ").append(type);

        if (notNull) {
            columnDef.append(" NOT NULL");
        }

        if (primaryKey) {
            columnDef.append(" PRIMARY KEY");
        }

        columns.add(columnDef.toString());
        return this;
    }

    public CreateTableBuilder column(String name, String type) {
        return column(name, type, false, false);
    }

    public CreateTableBuilder primaryKey(String... columns) {
        this.columns.add("PRIMARY KEY (" + String.join(", ", columns) + ")");
        return this;
    }

    public CreateTableBuilder foreignKey(String column, String refTable, String refColumn) {
        this.columns.add("FOREIGN KEY (" + column + ") REFERENCES " + refTable + "(" + refColumn + ")");
        return this;
    }

    @Override
    public String getSQL() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");

        if (ifNotExists) {
            sql.append("IF NOT EXISTS ");
        }

        sql.append(tableName).append(" (");
        sql.append(String.join(", ", columns));
        sql.append(")");

        return sql.toString();
    }
}

