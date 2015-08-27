package net.qiushao.qsandroidutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.qiushao.qsutils.log.Print;
import net.qiushao.qsutils.orm.DBAnnotationException;
import net.qiushao.qsutils.orm.DBFactory;
import net.qiushao.qsutils.orm.DBHelper;

import java.util.Collection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinkedList<Object> list = new LinkedList<Object>();
        for (int i = 0; i < 10000; i++) {
            Person person = new Person();
            person.id = i;
            person.name = "name" + i;
            person.age = 26;
            person.weight = 52;
            person.marry = false;
            list.add(person);
        }

        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPerson(list);
            }
        });

        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    qeuryPerson();
                } catch (DBAnnotationException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addPerson(LinkedList<Object> list) {
        long start = System.currentTimeMillis();
        DBHelper db = DBFactory.getInstance(this).getDBHelper(Person.class);
        db.insertAll(list);
        long end = System.currentTimeMillis();
        Print.d("time = " + (end - start));
    }

    private void qeuryPerson() throws DBAnnotationException {
        DBHelper db = DBFactory.getInstance(this).getDBHelper(Person.class);
        Collection<Object> persons = db.queryAll();
        for (Object object : persons) {
            Person person = (Person) object;
            Print.d("person.id = " + person.id);
            Print.d("person.name = " + person.name);
        }
    }
}
