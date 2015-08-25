package net.qiushao.qsutils.orm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by shaoqiu on 2015-8-21.
 */
public class DBHelper extends SQLiteOpenHelper {

    private DBInfo dbInfo;
    private SQLiteDatabase db;
    protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected final Lock readLock = readWriteLock.readLock();
    protected final Lock writeLock = readWriteLock.writeLock();

    DBHelper(Context context, DBInfo dbInfo) {
        super(context, dbInfo.getDbName(), null, dbInfo.getDbVersion());
        this.dbInfo = dbInfo;
        db = getWritableDatabase();
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
        writeLock.lock();
        try {
            db.execSQL(dbInfo.getInsertSql(), dbInfo.getValues(object));
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    public boolean insertAll(Collection<Object> objects) {
        writeLock.lock();
        try {
            db.beginTransaction();
            for(Object object : objects) {
                db.execSQL(dbInfo.getInsertSql(), dbInfo.getValues(object));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            writeLock.unlock();
        }
        return true;
    }

    public boolean clean() {
        writeLock.lock();
        try {
            db.beginTransaction();
            db.execSQL(dbInfo.getCleanSql());
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            writeLock.unlock();
        }
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



    public void execute(String sql) {
        db.execSQL(sql);
    }

    public void execute(String sql, Object[] bindArgs) {
        db.execSQL(sql, bindArgs);
    }
}
