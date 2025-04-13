package xyz.nullshade.nullcore.sql;

import org.checkerframework.checker.units.qual.C;
import xyz.nullshade.nullcore.sql.builders.*;

public final class SQL {

    private SQL() {
        //UTILITY
    }

    public static SelectBuilder select(String... columns) {
        return new SelectBuilder(columns);
    }

    public static InsertBuilder insertInto(String table) {
        return new InsertBuilder(table);
    }

    public static UpdateBuilder update(String table) {
        return new UpdateBuilder(table);
    }

    public static DeleteBuilder deleteFrom(String table) {
        return new DeleteBuilder(table);
    }

    public static CreateTableBuilder createTable(String tablename) {
        return new CreateTableBuilder(tablename);
    }


}
