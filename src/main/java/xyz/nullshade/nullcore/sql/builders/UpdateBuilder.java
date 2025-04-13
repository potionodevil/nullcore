package xyz.nullshade.nullcore.sql.builders;

public class UpdateBuilder extends AbstractWhereableBuilder<UpdateBuilder> {

    private boolean setAdded = false;

    public UpdateBuilder(String table) {
        sqlBuilder.append("UPDATE ").append(table);
    }

    public UpdateBuilder set(String column, Object value) {
        if (!setAdded) {
            sqlBuilder.append(" SET ");
            setAdded = true;
        } else {
            sqlBuilder.append(", ");
        }

        sqlBuilder.append(column).append(" = ?");
        parameters.add(value);
        return this;
    }
}

