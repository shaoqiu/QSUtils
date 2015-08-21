package net.qiushao.qsutils.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shaoqiu on 2015-8-21.
 */
class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, TableInfo info) {
        super(context, context.getPackageName(), null, info.tableVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
