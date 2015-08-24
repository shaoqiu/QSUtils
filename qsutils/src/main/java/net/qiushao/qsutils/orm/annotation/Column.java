package net.qiushao.qsutils.orm.annotation;

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
public @interface Column {

    /**
     * 指定该字段代表的列是否为主键，默认为否
     * 一个表中可以指定多个主键形成组合主键
     */
    boolean primary() default false;
}
