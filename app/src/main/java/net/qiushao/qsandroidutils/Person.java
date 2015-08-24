package net.qiushao.qsandroidutils;


import net.qiushao.qsutils.orm.annotation.Column;
import net.qiushao.qsutils.orm.annotation.Database;

/**
 * Created by shaoqiu on 15-8-22.
 */
@Database(version = 0)
public class Person {

    @Column(primary = true)
    public long id;

    @Column
    public String name;

    @Column
    public int age;

    @Column
    public double weight;

    @Column
    public boolean marry;
}
