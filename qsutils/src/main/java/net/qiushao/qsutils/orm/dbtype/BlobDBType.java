package net.qiushao.qsutils.orm.dbtype;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by shaoqiu on 2015-8-27.
 */
public class BlobDBType extends DBType {

    public BlobDBType() {
        type = Cursor.FIELD_TYPE_BLOB;
        name = "BLOB";
    }
    @Override
    public void bind(SQLiteStatement statement, int index, Object object) {
        statement.bindBlob(index, (byte[]) object);
    }

    @Override
    public Object getValue(Cursor cursor, int index) {
        return cursor.getBlob(index);
    }
}
