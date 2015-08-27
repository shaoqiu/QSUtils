package net.qiushao.qsutils.orm.dbtype;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by shaoqiu on 2015-8-27.
 */
public class IntegerDBType extends DBType {

    public IntegerDBType() {
        type = Cursor.FIELD_TYPE_INTEGER;
        name = "INTEGER";
    }
    @Override
    public void bind(SQLiteStatement statement, int index, Object object) {
        int value = (Integer) object;
        statement.bindLong(index, (long) value);
    }

    @Override
    public Object getValue(Cursor cursor, int index) {
        return cursor.getInt(index);
    }
}
