package net.qiushao.qsutils.orm.dbtype;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by shaoqiu on 2015-8-27.
 */
public class LongDBType extends DBType{
    public LongDBType() {
        type = Cursor.FIELD_TYPE_INTEGER;
        name = "INTEGER";
    }
    @Override
    public void bind(SQLiteStatement statement, int index, Object object) {
        statement.bindLong(index, (Long) object);
    }

    @Override
    public Object getValue(Cursor cursor, int index) {
        return cursor.getLong(index);
    }
}
