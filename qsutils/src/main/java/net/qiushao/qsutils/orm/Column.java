package net.qiushao.qsutils.orm;

import net.qiushao.qsutils.orm.dbtype.DBType;

import java.lang.reflect.Field;

/**
 * Created by shaoqiu on 2015-8-25.
 */
class Column {
    public String name = "";
    public DBType type = null;
    public int index = 0;
    public boolean primary = false;
    public Field field = null;
}
