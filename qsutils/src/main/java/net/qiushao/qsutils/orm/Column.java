package net.qiushao.qsutils.orm;

import android.database.Cursor;

import java.lang.reflect.Field;

/**
 * Created by shaoqiu on 2015-8-25.
 */
class Column {
    public String name = "";
    public int type = Cursor.FIELD_TYPE_NULL;
    public boolean primary = false;
    public Field field = null;
    public int index = 0;
}
