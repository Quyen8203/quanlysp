package vn.edu.stu.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import vn.edu.stu.myapplication.Database.Database;
import vn.edu.stu.myapplication.Model.Loai;

public class UpdateSPActivity extends AppCompatActivity {
    final String DATABASE_NAME = "datap.db";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    int id = -1;
    Cursor cursor;

    SQLiteDatabase database;

    Button btnChonHinh,  btnLuu, btnHuy;
    EditText txtTen, txtMota, txtLoai, txtGia;
    ImageView imgAVT;
    Spinner spinner;
    ArrayList<Loai> loais;
    String loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_s_p);

        addControls();
        addEvents();
        initUI();
        spinner();
    }

    private void addControls() {
        btnChonHinh = (Button) findViewById(R.id.btnChonHinh);

        btnLuu = (Button) findViewById(R.id.btnThem);
        btnHuy = (Button) findViewById(R.id.btnHuy);

        txtTen = (EditText) findViewById(R.id.txtTen);
        txtMota = (EditText) findViewById(R.id.txtMoTa);
        spinner = (Spinner) findViewById(R.id.spinner);
        txtGia = (EditText) findViewById(R.id.txtGia);

        imgAVT = (ImageView) findViewById(R.id.imgAVT);

        loais = new ArrayList<>();
    }

    private void addEvents() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
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

        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai = (String) getSelectItem(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);

        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT ID, Ten, MoTa, Anh, Gia FROM Phu WHERE ID = ?", new String[]{id + ""});
        cursor.moveToFirst();

        String Ten = cursor.getString(1);
        String Mota = cursor.getString(2);
        byte[] Anh = cursor.getBlob(3);
        Integer Gia = cursor.getInt(4);

        Bitmap bitmap = BitmapFactory.decodeByteArray(Anh, 0, Anh.length);
        imgAVT.setImageBitmap(bitmap);
        txtTen.setText(Ten);
        txtMota.setText(Mota);
        txtGia.setText(Gia + "");
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                Uri imgUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgAVT.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAVT.setImageBitmap(bitmap);
            }
        }

    }

    private void update() {
        String ten = txtTen.getText().toString();
        String mota = txtMota.getText().toString();
        String gia = txtGia.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgAVT);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten", ten);
        contentValues.put("MoTa", mota);
        contentValues.put("Anh", anh);
        contentValues.put("Loai", String.valueOf(loai));
        contentValues.put("Gia", gia + "");

        SQLiteDatabase database = Database.initDatabase(this, "datap.db");
        database.update("Phu", contentValues, "id = ?", new String[]{id + ""});

        Toast.makeText(getApplicationContext(), R.string.fix, Toast.LENGTH_LONG).show();

    }

    private void cancel() {
        Intent intent = new Intent(this, SPActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void spinner() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM Loai", null);
        loais.clear();
        while (cursor.moveToNext()) {

            int ID = cursor.getInt(0);
            String loai = cursor.getString(1);

            Loai type = new Loai(ID, loai);
            loais.add(type);
        }
        ArrayAdapter<Loai> adapterLoai = new ArrayAdapter<Loai>(this, android.R.layout.simple_spinner_dropdown_item, loais);
        adapterLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterLoai);

        cursor.close();
        database.close();
    }

    public String getSelectItem(View v) {
        Loai loai = (Loai) spinner.getSelectedItem();
        return loai.getLoai();
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
                        UpdateSPActivity.this,
                        AboutActivity.class);
                startActivity(manghinhAbout);

            case R.id.exit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}