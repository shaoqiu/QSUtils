package net.qiushao.qsutils.orm.dbtype;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by shaoqiu on 2015-8-27.
 */
public class DoubleDBType extends DBType {

    public DoubleDBType() {
        type = Cursor.FIELD_TYPE_FLOAT;
        name = "REAL";
    }

    @Override
    public void bind(SQLiteStatement statement, int index, Object object) {
        statement.bindDouble(index, (Double) object);
    }

    @Override
    public Object getValue(Cursor cursor, int index) {
        return cursor.getDouble(index);
    }
}
