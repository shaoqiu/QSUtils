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

    public synchronized DBHelper getDBHelper(Class<?> T) throws DBAnnotationException {
        String databaseName = T.getName().replace('.', '_');

        if (dbMap.containsKey(databaseName)) {
            return dbMap.get(databaseName);
        }

        Database database = T.getAnnotation(Database.class);
        if (database == null) {
            throw new DBAnnotationException("Class " + T.getName() + " not annotation by @Database!");
        }

        DBHelper db = new DBHelper(context, new DBInfo(T));
        dbMap.put(databaseName, db);
        return db;
    }
}
