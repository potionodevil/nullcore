package xyz.nullshade.nullcore.sql.builders;

public class DeleteBuilder extends AbstractWhereableBuilder<DeleteBuilder>
        implements LimitableStatement<DeleteBuilder> {

    public DeleteBuilder(String table) {
        sqlBuilder.append("DELETE FROM ").append(table);
    }

    @Override
    public DeleteBuilder limit(int limit) {
        sqlBuilder.append(" LIMIT ?");
        parameters.add(limit);
        return this;
    }

    @Override
    public DeleteBuilder offset(int offset) {
        sqlBuilder.append(" OFFSET ?");
        parameters.add(offset);
        return this;
    }
}

