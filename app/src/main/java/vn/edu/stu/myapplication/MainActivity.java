package vn.edu.stu.myapplication;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    EditText txtUserName, txtPass;
    Button btnLogin;
    CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //anh xa

        txtUserName = findViewById(R.id.txtUserName);
        txtPass =  findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        // Lấy dữ liệu từ SharedPreferences nếu có
        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("username", "");
        String savedPassword = prefs.getString("password", "");

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            txtUserName.setText(savedUsername);
            txtPass.setText(savedPassword);
            cbRememberMe.setChecked(true); // Nếu đã lưu mật khẩu, đánh dấu checkbox
        }

        //onclick
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "admin";
                String password = "123";
                if (txtUserName.getText().toString().equals(username) && txtPass.getText().toString().equals(password)) {
                    if (cbRememberMe.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences("userPrefs", MODE_PRIVATE).edit();
                        editor.putString("username", txtUserName.getText().toString());
                        editor.putString("password", txtPass.getText().toString());
                        editor.apply();
                    } else {
                        // Xóa thông tin nếu checkbox không được chọn
                        SharedPreferences.Editor editor = getSharedPreferences("userPrefs", MODE_PRIVATE).edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.apply();
                    }
                    Toast.makeText(getApplicationContext(), R.string.loginsuccess, Toast.LENGTH_LONG).show();

                    Intent mh = new Intent(MainActivity.this, SPActivity.class);
                    startActivity(mh);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.loginerror, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}