package net.qiushao.qsandroidutils;


import net.qiushao.qsutils.orm.annotation.Database;
import net.qiushao.qsutils.orm.annotation.Primary;

/**
 * Created by shaoqiu on 15-8-22.
 */
@Database(version = 11)
public class Person {
    @Primary
    public long id;
    public String name;
    public int age;
    public double weight;
    public boolean marry;
}
