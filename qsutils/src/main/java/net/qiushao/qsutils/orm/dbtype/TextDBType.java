package net.qiushao.qsutils.orm.dbtype;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by shaoqiu on 2015-8-27.
 */
public class TextDBType extends DBType {

    public TextDBType() {
        type = Cursor.FIELD_TYPE_STRING;
        name = "TEXT";
    }

    @Override
    public void bind(SQLiteStatement statement, int index, Object object) {
        statement.bindString(index, (String) object);
    }

    @Override
    public Object getValue(Cursor cursor, int index) {
        return cursor.getString(index);
    }
}
