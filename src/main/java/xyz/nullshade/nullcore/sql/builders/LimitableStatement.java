package xyz.nullshade.nullcore.sql.builders;

public interface LimitableStatement<T extends LimitableStatement<T>> {
    T limit(int limit);

    T offset(int offset);
}
