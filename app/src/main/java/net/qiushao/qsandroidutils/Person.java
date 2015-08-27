package net.qiushao.qsandroidutils;


import net.qiushao.qsutils.orm.annotation.Database;

/**
 * Created by shaoqiu on 15-8-22.
 */
@Database(version = 10)
public class Person {
    public long id;
    public String name;
    public int age;
    public double weight;
    public boolean marry;
}
