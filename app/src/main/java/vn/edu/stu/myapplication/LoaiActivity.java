package vn.edu.stu.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import vn.edu.stu.myapplication.Adapter.LoaiAdapter;
import vn.edu.stu.myapplication.Adapter.SPAdapter;
import vn.edu.stu.myapplication.Database.Database;
import vn.edu.stu.myapplication.Model.Loai;
import vn.edu.stu.myapplication.Model.SP;

public class LoaiActivity extends AppCompatActivity {

    final String DATABASE_NAME = "data.db";
    SQLiteDatabase database;
    ListView lvLoai;
    ArrayList<Loai> listLoai;
    ArrayList<SP> listSP;
    SPAdapter adapterTraiCay;
    LoaiAdapter adapterLoai;
    Button btnThemLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai);
        addControls();
        addEvents();
        readData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        readData();
    }

    private void addControls() {

        btnThemLoai = (Button) findViewById(R.id.btnThemLoai);

        lvLoai = (ListView) findViewById(R.id.lvLoai);
        listLoai = new ArrayList<>();
        adapterLoai = new LoaiAdapter(this, listLoai);
        lvLoai.setAdapter(adapterLoai);
    }

    private void addEvents() {
        btnThemLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manghinhAdd = new Intent(
                        LoaiActivity.this,
                        AddLoaiActivity.class);
                startActivity(manghinhAdd);
            }
        });

    }

    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT ID, Loai FROM Loai", null);
        listLoai.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            int ID = cursor.getInt(0);
            String Loai = cursor.getString(1);

            listLoai.add(new Loai(ID, Loai));
        }
        adapterLoai.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_loai, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.back:
                Intent manhinhSP = new Intent(this,SPActivity.class);
                startActivity(manhinhSP);
                break;
            case R.id.about:
                Intent manghinhAbout = new Intent(
                        LoaiActivity.this,
                        AboutActivity.class);
                startActivity(manghinhAbout);
                break;

            case R.id.exit:
                Intent intent= new Intent(LoaiActivity.this,SPActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}