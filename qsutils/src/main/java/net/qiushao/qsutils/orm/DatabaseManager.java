package net.qiushao.qsutils.orm;

/**
 * Created by shaoqiu on 2015-8-21.
 */
class DatabaseManager {

    private static DatabaseManager ourInstance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    private DatabaseManager() {
    }

}
