package xyz.nullshade.nullcore.sql.builders;

public interface WhereableStatement<T extends WhereableStatement<T>> {
    T where(String condition, Object... params);

    T and(String condition, Object... params);

    T or(String condition, Object... params);
}
