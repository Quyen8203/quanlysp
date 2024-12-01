package vn.edu.stu.myapplication;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import vn.edu.stu.myapplication.Adapter.SPAdapter;
import vn.edu.stu.myapplication.Database.Database;
import vn.edu.stu.myapplication.Model.SP;

public class SPActivity extends AppCompatActivity {
    final String DATABASE_NAME = "datap.db";
    SQLiteDatabase database;

    ListView lvTraiCay;
    ArrayList<SP> list;
    SPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_p);
        addControls();
        readData();
    }

    private void addControls() {
        lvTraiCay = (ListView) findViewById(R.id.lvTraiCay);
        list = new ArrayList<>();
        adapter = new SPAdapter(this, list);
        lvTraiCay.setAdapter(adapter);
    }

    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT ID, Ten, MoTa, Anh, Loai, Gia, Soluong FROM Phu", null);
        list.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            int ID = cursor.getInt(0);
            String Ten = cursor.getString(1);
            String MoTa = cursor.getString(2);
            byte[] Anh = cursor.getBlob(3);
            String Loai = cursor.getString(4);
            Integer Gia = cursor.getInt(5);
            Integer Soluong = cursor.getInt(6);

            list.add(new SP(ID, Ten, MoTa, Anh, Loai, Gia, Soluong));
        }
        adapter.notifyDataSetChanged();

        cursor.close();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_sp, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent manghinhAdd = new Intent(
                        SPActivity.this,
                        AddSPActivity.class);
                startActivity(manghinhAdd);
                break;
            case R.id.type:
                Intent manghinhKieu = new Intent(
                        SPActivity.this,
                        LoaiActivity.class);
                startActivity(manghinhKieu);
                break;
            case R.id.about:
                Intent manghinhAbout = new Intent(
                        SPActivity.this,
                        AboutActivity.class);
                startActivity(manghinhAbout);
                break;
            case R.id.exit:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}