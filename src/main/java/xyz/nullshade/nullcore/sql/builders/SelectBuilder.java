package xyz.nullshade.nullcore.sql.builders;

public class SelectBuilder extends AbstractWhereableBuilder<SelectBuilder>
        implements LimitableStatement<SelectBuilder> {

    private boolean fromAdded = false;

    public SelectBuilder(String... columns) {
        sqlBuilder.append("SELECT ");
        if (columns.length == 0) {
            sqlBuilder.append("*");
        } else {
            sqlBuilder.append(String.join(", ", columns));
        }
    }

    public SelectBuilder from(String table) {
        if (fromAdded) {
            throw new IllegalStateException("FROM clause already added");
        }
        sqlBuilder.append(" FROM ").append(table);
        fromAdded = true;
        return this;
    }

    public SelectBuilder join(String table, String condition) {
        if (!fromAdded) {
            throw new IllegalStateException("FROM clause must be added before JOIN");
        }
        sqlBuilder.append(" JOIN ").append(table).append(" ON ").append(condition);
        return this;
    }

    public SelectBuilder leftJoin(String table, String condition) {
        if (!fromAdded) {
            throw new IllegalStateException("FROM clause must be added before LEFT JOIN");
        }
        sqlBuilder.append(" LEFT JOIN ").append(table).append(" ON ").append(condition);
        return this;
    }

    public SelectBuilder rightJoin(String table, String condition) {
        if (!fromAdded) {
            throw new IllegalStateException("FROM clause must be added before RIGHT JOIN");
        }
        sqlBuilder.append(" RIGHT JOIN ").append(table).append(" ON ").append(condition);
        return this;
    }

    public SelectBuilder groupBy(String... columns) {
        sqlBuilder.append(" GROUP BY ").append(String.join(", ", columns));
        return this;
    }

    public SelectBuilder having(String condition, Object... params) {
        sqlBuilder.append(" HAVING ").append(condition);
        for (Object param : params) {
            parameters.add(param);
        }
        return this;
    }

    public SelectBuilder orderBy(String column, boolean ascending) {
        sqlBuilder.append(" ORDER BY ").append(column);
        if (!ascending) {
            sqlBuilder.append(" DESC");
        }
        return this;
    }

    @Override
    public SelectBuilder limit(int limit) {
        sqlBuilder.append(" LIMIT ?");
        parameters.add(limit);
        return this;
    }

    @Override
    public SelectBuilder offset(int offset) {
        sqlBuilder.append(" OFFSET ?");
        parameters.add(offset);
        return this;
    }
}
