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

public class AddLoaiActivity extends AppCompatActivity {
    final String DATABASE_NAME = "data.sqlite";

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

    private boolean isLoaiExists(String loai) {
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM Loai WHERE Loai = ?", new String[]{loai});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }


    private void cancel() {
        Intent intent = new Intent(this, LoaiActivity.class);
        startActivity(intent);
    }

    private void insert() {
        String loai = txtLoai.getText().toString();

        if (loai.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập tên loại sản phẩm!", Toast.LENGTH_LONG).show();
            return;
        }

        // Kiểm tra xem loại đã tồn tại chưa
        if (isLoaiExists(loai)) {
            Toast.makeText(getApplicationContext(), "Loại này đã tồn tại!", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("Loai", loai);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        database.insert("Loai", null, contentValues);
        database.close();

        Toast.makeText(getApplicationContext(), "Tạo thành công!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoaiActivity.class);
        startActivity(intent);
        finish();

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
                break;

            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}