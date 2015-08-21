package net.qiushao.qsutils.orm;

import java.util.Collection;

/**
 * Created by shaoqiu on 2015-8-21.
 */
public class ORMUtils {
    private static ORMUtils ourInstance = new ORMUtils();

    public static ORMUtils getInstance() {
        return ourInstance;
    }

    private ORMUtils() {
    }

    public boolean insert(Object object) {
        return true;
    }

    public boolean delete(Object object) {
        return true;
    }

    public boolean update(Object object) {
        return true;
    }

    public <T> T query(Class<?> T, String condiction) {
        return (T)null;
    }

    public <T> Collection<T> queryAll(Class<?> T) {
        return null;
    }
}
