package net.qiushao.qsutils.orm;

import android.database.Cursor;
import android.database.DatabaseUtils;

import net.qiushao.qsutils.orm.annotation.Column;
import net.qiushao.qsutils.orm.annotation.Database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by shaoqiu on 15-8-22.
 * every Class annotation with @Database is a database,
 * the class full name as the database name,
 * the class simple name as the table name
 */
class DBInfo {
    private String dbName;
    private int dbVersion;
    private String tableName;
    private String createTableSql;

    HashMap<String, ColumnInfo> columnsMap;

    public DBInfo(Class<?> T) {
        DatabaseUtils databaseUtils;
        Database database = T.getAnnotation(Database.class);
        dbName = T.getName().replace('.', '_');
        dbVersion = database.version();
        tableName = T.getSimpleName();

        columnsMap = new HashMap<String, ColumnInfo>();
        Field[] fields = T.getDeclaredFields();
        for(Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if(column == null) continue;
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.name = field.getName();
            columnInfo.type = convertToDBType(field.getType());
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

    private class ColumnInfo {
        String name;
        int type;
    }

    private int convertToDBType(Class<?> type) {
        if ( instanceof byte[]) {
            return Cursor.FIELD_TYPE_BLOB;
        } else if (obj instanceof Float || obj instanceof Double) {
            return Cursor.FIELD_TYPE_FLOAT;
        } else if (obj instanceof Long || obj instanceof Integer
                || obj instanceof Short || obj instanceof Byte) {
            return Cursor.FIELD_TYPE_INTEGER;
        } else {
            return Cursor.FIELD_TYPE_STRING;
        }
        return 0;
    }
}
