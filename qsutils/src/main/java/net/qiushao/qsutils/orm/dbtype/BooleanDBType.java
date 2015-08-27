package net.qiushao.qsutils.orm.dbtype;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by shaoqiu on 2015-8-27.
 */
public class BooleanDBType extends DBType {
    @Override
    public void bind(SQLiteStatement statement, int index, Object object) {
        if((Boolean) object) {
            statement.bindLong(index, 1);
        } else {
            statement.bindLong(index, 0);
        }
    }

    @Override
    public Object getValue(Cursor cursor, int index) {
        int b = cursor.getInt(index);
        return b == 1;
    }
}
