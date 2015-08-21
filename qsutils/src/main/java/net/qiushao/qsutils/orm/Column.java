package net.qiushao.qsutils.orm;

import android.database.Cursor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shaoqiu on 2015-8-21.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Column {

    /**
     * 指定该字段代表的列在数据库中的名称，不指定则使用变量本身名称。
     */
    String name() default "";

    /**
     * 指定该字段代表的列在数据库中的类型。
     * 不指定的话，则根据数据类型自动转换，转换规则如下：
     * NULL : null
     * INTEGER : byte, short, int, long, boolean 及以上所有类型的包装类
     * REAL : float, double 及以上所有类型的包装类
     * TEXT : String, char, char[]
     * BLOB : byte[]
     */
    int type() default Cursor.FIELD_TYPE_NULL;

    /**
     * 指定该字段代表的列是否为主键，默认为否
     * 一个表中可以指定多个主键形成组合主键
     */
    boolean primary() default false;
}
