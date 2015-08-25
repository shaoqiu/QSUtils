package net.qiushao.qsandroidutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.qiushao.qsutils.orm.DBAnnotationException;
import net.qiushao.qsutils.orm.DBFactory;
import net.qiushao.qsutils.orm.DBHelper;

public class MainActivity extends AppCompatActivity {
    private long id = 1;

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
    }

    private void addPerson() {
        Person person = new Person();
        person.id = id++;
        person.name = "name" + person.id;
        person.age = 18;
        person.weight = 520.1314;
        person.marry = false;
        try {
            DBHelper db = DBFactory.getInstance(this).getDBHelper(Person.class);
            db.insert(person);
        } catch (DBAnnotationException e) {
            e.printStackTrace();
        }
    }
}
