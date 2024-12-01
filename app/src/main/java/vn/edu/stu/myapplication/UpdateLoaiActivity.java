package vn.edu.stu.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vn.edu.stu.myapplication.Database.Database;

public class UpdateLoaiActivity extends AppCompatActivity {
    final String DATABASE_NAME = "data.db";
    int id = -1;
    Cursor cursor;
    SQLiteDatabase database;
    Button btnThem, btnHuy;
    EditText txtLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_loai);
        addControls();
        addEvents();
        initUI();
    }

    private void addControls() {
        btnThem = (Button) findViewById(R.id.btnThem);
        btnHuy = (Button) findViewById(R.id.btnHuy);

        txtLoai = (EditText) findViewById(R.id.txtLoai);
    }

    private void addEvents() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);

        if (id == -1) {
            Toast.makeText(UpdateLoaiActivity.this, "Loi", Toast.LENGTH_SHORT).show();
        }
        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT ID, Loai FROM Loai WHERE ID = ?", new String[]{id + ""});
        cursor.moveToFirst();

        String Loai = cursor.getString(1);

        txtLoai.setText(Loai);

        cursor.close();
        database.close();
    }

    private void cancel() {
        Intent intent = new Intent(this, LoaiActivity.class);
        startActivity(intent);
    }

    private void update() {
        String loai = txtLoai.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Loai", loai);

        SQLiteDatabase database = Database.initDatabase(this, "data.db");
        database.update("Loai", contentValues, "id = ?", new String[]{id + ""});

        database.close();

        Toast.makeText(getApplicationContext(), R.string.fix, Toast.LENGTH_LONG).show();

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
                        UpdateLoaiActivity.this,
                        AboutActivity.class);
                startActivity(manghinhAbout);

            case R.id.exit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}