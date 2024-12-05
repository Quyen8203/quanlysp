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
import vn.edu.stu.myapplication.Model.SP;

public class AddSPActivity extends AppCompatActivity {
    final String DATABASE_NAME = "data.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    SQLiteDatabase database;
    Button btnChonHinh,  btnThem, btnHuy, btnChuphinh;
    EditText txtTen, txtMota, txtGia,txtSL;
    Spinner spinner;
    ImageView imgAVT;
    ArrayList<Loai> loais;
    ArrayList<SP> list;
    String loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_s_p);
        addControls();

        addEvents();
        spinner();


    }

    private void addControls() {
        btnChonHinh = findViewById(R.id.btnChonHinh);
        btnChuphinh = findViewById(R.id.btnChupHinh);

        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);

        txtTen = findViewById(R.id.txtTen);
        txtMota = findViewById(R.id.txtMoTa);
        spinner = findViewById(R.id.spinner);
        txtGia = findViewById(R.id.txtGia);
        txtSL = findViewById(R.id.txtSL);

        imgAVT = findViewById(R.id.imgAVT);

        loais = new ArrayList<>();
    }

    private void addEvents() {
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

        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        btnChuphinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai = getSelectItem(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void insert() {
        String ten = txtTen.getText().toString();
        String mota = txtMota.getText().toString();
        String gia = txtGia.getText().toString().trim();
        byte[] anh = getByteArrayFromImageView(imgAVT);
        String soluong = txtSL.getText().toString().trim();

        if (ten.isEmpty() || mota.isEmpty() || gia.isEmpty() || soluong.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }


        int gia1;
        int soluong1;

        try {
            gia1 = Integer.parseInt(gia);
            soluong1 = Integer.parseInt(soluong);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá và số lượng phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra giá trị âm
        if (gia1 <= 0 || soluong1 <= 0) {
            Toast.makeText(this, "Giá và số lượng phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten", ten);
        contentValues.put("MoTa", mota);
        contentValues.put("Anh", anh);
        contentValues.put("Loai",String.valueOf(loai));
        contentValues.put("Gia", gia);
        contentValues.put("Soluong", soluong1);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        database.insert("Phu", null, contentValues);

        Toast.makeText(getApplicationContext(), " Tạo thành công", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, SPActivity.class);
        startActivity(intent);

    }

    private void cancel() {
        Intent intent = new Intent(this, SPActivity.class);
        startActivity(intent);
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

    private byte[] getByteArrayFromImageView(ImageView imgv) {

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void spinner() {
        database = openOrCreateDatabase("data.sqlite", MODE_PRIVATE, null);

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
                        AddSPActivity.this,
                        AboutActivity.class);
                startActivity(manghinhAbout);

            case R.id.exit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }



}