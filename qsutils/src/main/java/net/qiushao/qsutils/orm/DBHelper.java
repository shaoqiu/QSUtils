package net.qiushao.qsutils.orm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collection;

/**
 * Created by shaoqiu on 2015-8-21.
 */
public class DBHelper extends SQLiteOpenHelper {
    DBInfo dbInfo;

    public DBHelper(Context context, DBInfo dbInfo) {
        super(context, dbInfo.getDbName(), null, dbInfo.getDbVersion());
        this.dbInfo = dbInfo;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbInfo.getCreateTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbInfo.getTableName());
        onCreate(db);
    }

    public boolean insert(Object object) {
        return true;
    }
    public boolean insertAll(Collection<Object> objects) {
        return true;
    }

    public boolean delete(Object object) {
        return true;
    }
    public boolean delete(Collection<Object> objects) {
        return true;
    }

    public boolean update(Object object) {
        return true;
    }

    public boolean update(Collection<Object> objects) {
        return true;
    }

    public <T> Collection<T> queryAll() {
        return null;
    }

    public Cursor query(String sql, Object[] args) {
        return null;
    }

    public <T> Collection<T> queryByColumns(String[] columns, Object[] values) {
        return null;
    }

    public boolean clean() {
        return true;
    }

    public void execute(String sql, Object[] bindArgs) {
        getWritableDatabase().execSQL(sql, bindArgs);
    }
}
