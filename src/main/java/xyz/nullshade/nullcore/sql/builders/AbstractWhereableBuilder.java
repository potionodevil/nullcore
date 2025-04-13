package xyz.nullshade.nullcore.sql.builders;

public abstract class AbstractWhereableBuilder<T extends AbstractWhereableBuilder<T>>
        extends AbstractQueryBuilder<T> implements WhereableStatement<T> {

    protected boolean whereClauseAdded = false;

    @Override
    public T where(String condition, Object... params) {
        if (whereClauseAdded) {
            throw new IllegalStateException("WHERE clause already added. Use AND or OR instead.");
        }
        sqlBuilder.append(" WHERE ").append(condition);
        for (Object param : params) {
            parameters.add(param);
        }
        whereClauseAdded = true;
        return self();
    }

    @Override
    public T and(String condition, Object... params) {
        if (!whereClauseAdded) {
            throw new IllegalStateException("WHERE clause must be added before AND");
        }
        sqlBuilder.append(" AND ").append(condition);
        for (Object param : params) {
            parameters.add(param);
        }
        return self();
    }

    @Override
    public T or(String condition, Object... params) {
        if (!whereClauseAdded) {
            throw new IllegalStateException("WHERE clause must be added before OR");
        }
        sqlBuilder.append(" OR ").append(condition);
        for (Object param : params) {
            parameters.add(param);
        }
        return self();
    }
}
