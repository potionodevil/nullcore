package xyz.nullshade.nullcore.sql.builders;

import java.util.ArrayList;
import java.util.List;

public class InsertBuilder extends AbstractSQLBuilder<InsertBuilder> {

    private final String table;
    private final List<String> columns = new ArrayList<>();
    private boolean columnsAdded = false;
    private boolean valuesAdded = false;

    public InsertBuilder(String table) {
        this.table = table;
        sqlBuilder.append("INSERT INTO ").append(table);
    }

    public InsertBuilder columns(String... cols) {
        if (columnsAdded) {
            throw new IllegalStateException("Columns already defined");
        }
        columns.addAll(List.of(cols));
        sqlBuilder.append(" (").append(String.join(", ", cols)).append(")");
        columnsAdded = true;
        return this;
    }

    public InsertBuilder values(Object... values) {
        if (!columnsAdded) {
            throw new IllegalStateException("Columns must be defined before values");
        }
        if (valuesAdded) {
            sqlBuilder.append(", ");
        } else {
            sqlBuilder.append(" VALUES ");
            valuesAdded = true;
        }

        if (values.length != columns.size()) {
            throw new IllegalArgumentException(
                    "Number of values (" + values.length + ") does not match number of columns (" + columns.size() + ")");
        }

        StringBuilder placeholders = new StringBuilder();
        placeholders.append("(");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                placeholders.append(", ");
            }
            placeholders.append("?");
            parameters.add(values[i]);
        }
        placeholders.append(")");
        sqlBuilder.append(placeholders);

        return this;
    }

    public InsertBuilder onDuplicateKeyUpdate(String... updates) {
        sqlBuilder.append(" ON DUPLICATE KEY UPDATE ");
        sqlBuilder.append(String.join(", ", updates));
        return this;
    }
}
