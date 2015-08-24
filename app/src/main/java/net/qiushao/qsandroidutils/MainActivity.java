package net.qiushao.qsandroidutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;

import net.qiushao.qsutils.orm.DBAnnotationException;
import net.qiushao.qsutils.orm.DBFactory;
import net.qiushao.qsutils.orm.DBHelper;

public class MainActivity extends AppCompatActivity {
    private long id = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView cd = (ImageView) findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNetError();
                addPerson();
            }
        });
    }

    private View networkErrorView;

    private void showNetError() {
        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.VISIBLE);
            return;
        }
        ViewStub stub = (ViewStub) findViewById(R.id.network_error_viewstub);
        networkErrorView = stub.inflate();
        Button refresh = (Button) findViewById(R.id.refresh);
    }

    private void showNormal() {
        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.GONE);
        }
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
