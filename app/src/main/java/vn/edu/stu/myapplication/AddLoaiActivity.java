package vn.edu.stu.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vn.edu.stu.myapplication.Database.Database;

public class AddLoaiActivity extends AppCompatActivity {
    final String DATABASE_NAME = "datap.db";

    Button btnThem, btnHuy;
    EditText txtLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loai);
        addControls();
    }

    private void addControls() {
        btnThem = (Button) findViewById(R.id.btnThem);
        btnHuy = (Button) findViewById(R.id.btnHuy);

        txtLoai = (EditText) findViewById(R.id.txtLoai);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void cancel() {
        Intent intent = new Intent(this, LoaiActivity.class);
        startActivity(intent);
    }

    private void insert() {
        String loai = txtLoai.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Loai", loai);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        database.insert("Loai", null, contentValues);
        //trở về

        database.close();
        Toast.makeText(getApplicationContext(), " Tạo thành công", Toast.LENGTH_LONG).show();
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
            case R.id.about:
                Intent manghinhAbout = new Intent(
                        AddLoaiActivity.this,
                        AboutActivity.class);
                startActivity(manghinhAbout);

            case R.id.exit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}