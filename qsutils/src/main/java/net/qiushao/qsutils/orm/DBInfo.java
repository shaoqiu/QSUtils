package net.qiushao.qsutils.orm;

import android.database.Cursor;

import net.qiushao.qsutils.orm.annotation.Database;
import net.qiushao.qsutils.orm.annotation.Primary;
import net.qiushao.qsutils.orm.annotation.Transient;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by shaoqiu on 15-8-22.
 * every Class annotation with @Database is a database,
 * the class full name as the database name,
 * the class simple name as the table name
 */
class DBInfo {
    private Class<?> T;
    private String dbName;
    private int dbVersion;
    private String tableName;

    private String createTableSql;
    private String insertSql;
    private String cleanSql;

    HashMap<String, Column> columnsMap;
    LinkedList<Column> primaryColumns;

    public DBInfo(Class<?> T) {
        this.T = T;
        Database database = T.getAnnotation(Database.class);
        dbName = T.getName().replace('.', '_') + ".db";
        dbVersion = database.version();
        tableName = T.getSimpleName();

        columnsMap = new HashMap<String, Column>();
        primaryColumns = new LinkedList<Column>();

        Field[] fields = T.getDeclaredFields();
        for (Field field : fields) {
            if (!isSurpportType(field.getType())) continue;
            if (field.getAnnotation(Transient.class) != null) continue;

            Column column = new Column();
            column.name = field.getName();
            column.type = convertToDBType(field.getType());
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
    }

    public String getDbName() {
        return dbName;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCreateTableSql() {
        return createTableSql;
    }

    public String getInsertSql() {
        return insertSql;
    }


    private static HashMap<Class<?>, Integer> dbTypeMap;

    static {
        dbTypeMap = new HashMap<Class<?>, Integer>();
        dbTypeMap.put(String.class, Cursor.FIELD_TYPE_STRING);
        dbTypeMap.put(char[].class, Cursor.FIELD_TYPE_STRING);

        dbTypeMap.put(byte.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(Byte.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(char.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(Character.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(short.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(Short.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(int.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(Integer.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(long.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(Long.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(boolean.class, Cursor.FIELD_TYPE_INTEGER);
        dbTypeMap.put(Boolean.class, Cursor.FIELD_TYPE_INTEGER);

        dbTypeMap.put(float.class, Cursor.FIELD_TYPE_FLOAT);
        dbTypeMap.put(Float.class, Cursor.FIELD_TYPE_FLOAT);
        dbTypeMap.put(double.class, Cursor.FIELD_TYPE_FLOAT);
        dbTypeMap.put(Double.class, Cursor.FIELD_TYPE_FLOAT);

        dbTypeMap.put(byte[].class, Cursor.FIELD_TYPE_BLOB);
    }

    private boolean isSurpportType(Class<?> type) {
        return dbTypeMap.containsKey(type);
    }

    private int convertToDBType(Class<?> type) {
        return dbTypeMap.get(type);
    }

    private static String[] dbTypeName;

    static {
        dbTypeName = new String[5];
        dbTypeName[Cursor.FIELD_TYPE_NULL] = "NULL";
        dbTypeName[Cursor.FIELD_TYPE_BLOB] = "BLOB";
        dbTypeName[Cursor.FIELD_TYPE_FLOAT] = "REAL";
        dbTypeName[Cursor.FIELD_TYPE_INTEGER] = "INTEGER";
        dbTypeName[Cursor.FIELD_TYPE_STRING] = "TEXT";
    }

    private String getDBTypeName(int type) {
        return dbTypeName[type];
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
            sql.append(getDBTypeName(column.type));
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

    public String getCleanSql() {
        return cleanSql;
    }

    public Object[] getValues(Object object) {
        Object[] values = new Object[columnsMap.size()];
        Iterator iterator = columnsMap.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Column column = (Column) entry.getValue();
                values[column.index] = column.field.get(object);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return values;
    }
}
