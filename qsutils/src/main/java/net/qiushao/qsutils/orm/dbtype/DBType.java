package net.qiushao.qsutils.orm.dbtype;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.HashMap;

/**
 * Created by shaoqiu on 2015-8-27.
 */
public abstract class DBType {
    public int type;
    public String name;
    public abstract void bind(SQLiteStatement statement, int index, Object object);
    public abstract Object getValue(Cursor cursor, int index);

    private static HashMap<Class<?>, DBType> dbTypeMap;
    static {
        dbTypeMap = new HashMap<Class<?>, DBType>();
        DBType intergerType = new IntegerDBType();
        DBType longType = new LongDBType();
        DBType doubleType = new DoubleDBType();
        DBType textType = new TextDBType();
        DBType blobType = new BlobDBType();
        DBType booleanType = new BooleanDBType();

        dbTypeMap.put(String.class, textType);
        dbTypeMap.put(char[].class, textType);

        dbTypeMap.put(byte.class, intergerType);
        dbTypeMap.put(Byte.class, intergerType);
        dbTypeMap.put(char.class, intergerType);
        dbTypeMap.put(Character.class, intergerType);
        dbTypeMap.put(short.class, intergerType);
        dbTypeMap.put(Short.class, intergerType);
        dbTypeMap.put(int.class, intergerType);
        dbTypeMap.put(Integer.class, intergerType);

        dbTypeMap.put(long.class, longType);
        dbTypeMap.put(Long.class, longType);

        dbTypeMap.put(boolean.class, booleanType);
        dbTypeMap.put(Boolean.class, booleanType);

        dbTypeMap.put(float.class, doubleType);
        dbTypeMap.put(Float.class, doubleType);
        dbTypeMap.put(double.class, doubleType);
        dbTypeMap.put(Double.class, doubleType);

        dbTypeMap.put(byte[].class, blobType);
    }

    public static DBType getDBType(Class<?> T) {
        return dbTypeMap.get(T);
    }

    public static boolean isSurpportType(Class<?> type) {
        return dbTypeMap.containsKey(type);
    }
}
