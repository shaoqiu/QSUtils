package net.qiushao.qsandroidutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.qiushao.qsutils.log.Print;
import net.qiushao.qsutils.orm.DBFactory;
import net.qiushao.qsutils.orm.DBHelper;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPerson();
            }
        });

        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qeuryPerson();
            }
        });
    }

    private void addPerson() {

        DBHelper db = DBFactory.getInstance(this).getDBHelper(Person.class);

        Person person = new Person();
        person.id = 4;
        person.name = "shaoqiu";
        person.age = 26;
        person.weight = 53.5;
        person.marry = true;
        db.insert(person);

        person.id = 5;
        person.name = "junjun";
        person.age = 23;
        person.weight = 50.5;
        person.marry = false;
        db.insert(person);

        person.id = 6;
        person.name = "qiuqiu";
        person.age = 25;
        person.weight = 52.5;
        person.marry = false;
        db.insert(person);
    }

    private void qeuryPerson() {
        DBHelper db = DBFactory.getInstance(this).getDBHelper(Person.class);
        Collection<Object> persons = db.query(new String[]{"name"}, new String[]{"junjun"});

        for (Object object : persons) {
            Person person = (Person) object;
            Print.d("person.id = " + person.id);
            Print.d("person.name = " + person.name);
            Print.d("person.age = " + person.age);
            Print.d("person.wage = " + person.weight);
            Print.d("person.marry = " + person.marry);
        }
    }
}
