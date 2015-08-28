package net.qiushao.qsutils.orm;

import android.content.Context;

import net.qiushao.qsutils.orm.annotation.Database;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shaoqiu on 2015-8-21.
 */
public class DBFactory {
    private static DBFactory instance;
    private ConcurrentHashMap<String, DBHelper> dbMap;
    private Context context;

    public static DBFactory getInstance(Context context) {
        if (instance == null) {
            synchronized (DBFactory.class) {
                if (instance == null) {
                    instance = new DBFactory(context);
                }
            }
        }
        return instance;
    }

    private DBFactory(Context context) {
        this.context = context.getApplicationContext();
        dbMap = new ConcurrentHashMap<String, DBHelper>();
    }

    public synchronized DBHelper getDBHelper(Class<?> claz) {
        Database database = claz.getAnnotation(Database.class);
        if(database == null) return null;

        String databaseName = claz.getName().replace('.', '_') + ".db";
        if (dbMap.containsKey(databaseName)) {
            return dbMap.get(databaseName);
        }
        DBHelper db = new DBHelper(context, databaseName, database.version(), claz);
        dbMap.put(databaseName, db);
        return db;
    }
}
