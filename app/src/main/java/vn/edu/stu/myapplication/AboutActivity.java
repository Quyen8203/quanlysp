package vn.edu.stu.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView txtPhoneNum,txtEmail;
    Button btnCall,btnEmail;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);

        addControls();
        addEvents();
    }

    private void addControls() {
        txtPhoneNum = findViewById(R.id.txtPhoneNum);
        txtEmail = findViewById(R.id.txtEmail);
        btnCall = findViewById(R.id.btnCall);
        btnEmail = findViewById(R.id.btnEmail);
    }

    private void addEvents() {
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    makePhoneCall();
                } else {
                    requestPermissions();
                }
            }
        });
        btnEmail.setOnClickListener(view -> sentEmail());
    }

    private void sentEmail() {
        String emailAddress = txtEmail.getText().toString(); // Địa chỉ email gửi đến
        String subject = ""; // Tiêu đề email
        String body = ""; // Nội dung email

        // Tạo intent gửi email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailAddress));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);  // Thêm tiêu đề email
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);  // Thêm nội dung email

        // Kiểm tra xem có ứng dụng email nào để mở không
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(AboutActivity.this, "Không có ứng dụng gửi email", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                AboutActivity.this,
                Manifest.permission.CALL_PHONE
        )) {
            Toast.makeText(
                    AboutActivity.this,
                    "Vui lòng cấp quyền thủ công trong App Setting",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            ActivityCompat.requestPermissions(
                    AboutActivity.this,
                    new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    123
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(
                        AboutActivity.this,
                        "Bạn đã từ chối cấp quyền gọi. Hủy thao tác.",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(
                AboutActivity.this,
                Manifest.permission.CALL_PHONE
        );
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void makePhoneCall() {
        String phoneNum = txtPhoneNum.getText().toString();
        Intent callIntent = new Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:" + phoneNum)
        );
        startActivity(callIntent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

    }
}