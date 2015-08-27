package net.qiushao.qsutils.orm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import net.qiushao.qsutils.orm.annotation.Primary;
import net.qiushao.qsutils.orm.annotation.Transient;
import net.qiushao.qsutils.orm.dbtype.DBType;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by shaoqiu on 2015-8-21.
 */
public class DBHelper extends SQLiteOpenHelper {
    private Class<?> claz;
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;
    protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected final Lock readLock = readWriteLock.readLock();
    protected final Lock writeLock = readWriteLock.writeLock();

    private String dbName;
    private int dbVersion;
    private String tableName;
    private String createTableSql;
    private String insertSql;
    private String cleanSql;
    private HashMap<String, Column> columnsMap;
    private LinkedList<Column> primaryColumns;

    DBHelper(Context context, String name, int version, Class claz) {
        super(context, name, null, version);
        dbName = name;
        dbVersion = version;
        this.claz = claz;
        initDatabaseInfo();
        db = getWritableDatabase();
        insertStatement = db.compileStatement(insertSql);
    }

    private void initDatabaseInfo() {
        tableName = claz.getSimpleName();
        columnsMap = new HashMap<String, Column>();
        primaryColumns = new LinkedList<Column>();

        Field[] fields = claz.getDeclaredFields();
        for (Field field : fields) {
            if (!DBType.isSurpportType(field.getType())) continue;
            if (field.getAnnotation(Transient.class) != null) continue;

            Column column = new Column();
            column.name = field.getName();
            column.type = DBType.getDBType(field.getType());
            column.field = field;
            column.field.setAccessible(true);
            if (field.getAnnotation(Primary.class) != null) {
                column.primary = true;
                primaryColumns.add(column);
            }

            columnsMap.put(column.name, column);

            createTableSql = genCreateTableSql();
            insertSql = genInsertSql();
            cleanSql = "DELETE FROM " + tableName;
        }

        createTableSql = genCreateTableSql();
        insertSql = genInsertSql();
        cleanSql = "DELETE FROM " + tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    public boolean insert(Object object) {
        writeLock.lock();
        try {
            bindInsertStatementArgs(object);
            insertStatement.executeInsert();
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    public boolean insertAll(Collection<Object> objects) {
        writeLock.lock();
        db.beginTransaction();
        try {
            for(Object object : objects) {
                bindInsertStatementArgs(object);
                insertStatement.executeInsert();
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
        db.beginTransaction();
        try {
            db.execSQL(cleanSql);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            writeLock.unlock();
        }
        return true;
    }

    private String genQuerySql(String[] columns) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ");
        sql.append(tableName);
        sql.append(" where ");

        for(String column : columns) {
            sql.append(column);
            sql.append(" = ? and ");
        }

        //delete last and word
        sql.delete(sql.length() - 5, sql.length() - 1);
        return sql.toString();
    }

    /**
     * query by the columns value
     * execute as "select * from table where column1 = arg1 and column2 = arg2 ..."
     * @param columns
     * @param args
     * @return return where clumns's values equals args objects
     */
    public Collection<Object> query(String[] columns, String[] args) {
        readLock.lock();
        try {
            if(columns == null || columns.length == 0) return queryAll();
            Cursor cursor = db.rawQuery(genQuerySql(columns), args);
            return cursorToObjects(cursor);
        } finally {
            readLock.unlock();
        }
    }

    public Collection<Object> query(String sql, String[] args) {
        readLock.lock();
        try {
            Cursor cursor = db.rawQuery(sql, args);
            return cursorToObjects(cursor);
        } finally {
            readLock.unlock();
        }
    }

    public Cursor queryForCursor(String[] columns, String[] args) {
        readLock.lock();
        try {
            return db.rawQuery(genQuerySql(columns), args);
        } finally {
            readLock.unlock();
        }
    }

    public Cursor queryForCursor(String sql, String[] args) {
        readLock.lock();
        try {
            return db.rawQuery(sql, args);
        } finally {
            readLock.unlock();
        }
    }

    public Collection<Object> queryAll() {
        readLock.lock();
        try {
            Cursor cursor = db.rawQueryWithFactory(
                    null, //cursorFactory
                    "select * from " + tableName, //sql
                    null, //selectionArgs
                    tableName, //table
                    null //cancellationSignal
            );
            return cursorToObjects(cursor);
        } finally {
            readLock.unlock();
        }
    }

    private Collection<Object> cursorToObjects(Cursor cursor) {
        LinkedList<Object> list = new LinkedList<Object>();
        if(cursor != null) {
            while (cursor.moveToNext()) {
                Object object = newInstance(cursor);
                if (object != null) {
                    list.add(object);
                }
            }
        }
        cursor.close();
        return list;
    }

    public void execute(String sql) {
        db.execSQL(sql);
    }

    public void execute(String sql, Object[] bindArgs) {
        db.execSQL(sql, bindArgs);
    }

    private void bindInsertStatementArgs(Object object) {
        Iterator iterator = columnsMap.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Column column = (Column) entry.getValue();
                column.type.bind(insertStatement, column.index + 1, column.field.get(object));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object newInstance(Cursor cursor) {
        Object object = null;
        try {
            object = claz.newInstance();
            Iterator iterator = columnsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Column column = (Column) entry.getValue();
                column.field.set(object, column.type.getValue(cursor, column.index));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    private String genCreateTableSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(tableName);
        sql.append("(");

        int index = 0;
        Iterator iterator = columnsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Column column = (Column) entry.getValue();
            column.index = index;
            index++;
            sql.append(column.name);
            sql.append(" ");
            sql.append(column.type.name);
            sql.append(",");
        }

        if (primaryColumns.size() > 0) {
            sql.append("PRIMARY KEY(");
            for (Column column : primaryColumns) {
                sql.append(column.name);
                sql.append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
        } else {
            sql.deleteCharAt(sql.length() - 1);
        }

        sql.append(")");

        return sql.toString();
    }

    private String genInsertSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append("(");

        StringBuilder values = new StringBuilder();
        values.append(" VALUES(");

        Iterator iterator = columnsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            sql.append(entry.getKey());
            sql.append(",");
            values.append("?,");
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        values.deleteCharAt(values.length() - 1);
        values.append(")");

        sql.append(values);
        return sql.toString();
    }
}
