package vn.edu.stu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.stu.myapplication.Database.Database;

public class ChiTietSPActivity extends AppCompatActivity {
    final String DATABASE_NAME = "data.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    SQLiteDatabase database;

    ImageView imageviewHinh;
    TextView textviewMa, textviewTen, textviewMota, textviewLoai, textviewGia,textviewSL;
    Cursor cursor;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_s_p);
        addControls();
        addEvents();
        initUI();
    }

    private void addControls() {
        textviewMa = (TextView) findViewById(R.id.textviewMa);
        textviewTen = (TextView) findViewById(R.id.textviewTen);
        textviewMota = (TextView) findViewById(R.id.textviewMota);
        textviewLoai = (TextView) findViewById(R.id.textviewLoai);
        textviewGia = (TextView) findViewById(R.id.textviewGia);
        textviewSL = findViewById(R.id.textviewSoLuong);
        imageviewHinh = (ImageView) findViewById(R.id.imageviewHinh);
    }

    private void addEvents() {

    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);

        if (id == -1) {
            Toast.makeText(ChiTietSPActivity.this, "Loi", Toast.LENGTH_SHORT).show();
        }

        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT ID, Ten, MoTa, Anh, Loai, Gia, Soluong FROM Phu WHERE ID = ?", new String[]{id + ""});
        cursor.moveToFirst();

        int ID = cursor.getInt(0);
        String Ten = cursor.getString(1);
        String Mota = cursor.getString(2);
        byte[] Anh = cursor.getBlob(3);
        String Loai = cursor.getString(4);
        Integer Gia = cursor.getInt(5);
        Integer Soluong = cursor.getInt(6);


        Bitmap bitmap = BitmapFactory.decodeByteArray(Anh, 0, Anh.length);

        imageviewHinh.setImageBitmap(bitmap);
        textviewMa.setText(ID+"");
        textviewTen.setText(Ten);
        textviewMota.setText(Mota);
        textviewLoai.setText(Loai);
        textviewGia.setText(Gia+"");
        textviewSL.setText(Soluong+"");


        cursor.close();
        database.close();
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
                        ChiTietSPActivity.this,
                        AboutActivity.class);
                startActivity(manghinhAbout);

            case R.id.exit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}