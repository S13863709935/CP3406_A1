package com.example.weatherforecastd9k;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.weatherforecastd9k.data.AppDatabase;
import com.example.weatherforecastd9k.data.User;
import com.example.weatherforecastd9k.viewmodel.UserViewModel;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private TextInputEditText phoneInput;
    private TextInputEditText verificationCodeInput;
    private MaterialButton sendCodeButton;
    private MaterialButton registerButton;
    private TextView loginLink;
    private String verificationCode;
    private AppDatabase db;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        initViews();
        setupListeners();
        observeViewModel();
    }

    private void initViews() {
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        phoneInput = findViewById(R.id.phone);
        verificationCodeInput = findViewById(R.id.verificationCode);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
    }

    private void observeViewModel() {
        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getVerificationCode().observe(this, code -> {
            if (code != null) {
                Toast.makeText(this, "验证码: " + code, Toast.LENGTH_LONG).show();
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        sendCodeButton.setEnabled(false);
                        sendCodeButton.setText(millisUntilFinished / 1000 + "秒后重试");
                    }

                    public void onFinish() {
                        sendCodeButton.setEnabled(true);
                        sendCodeButton.setText("发送验证码");
                    }
                }.start();
            }
        });
    }

    private void setupListeners() {
        sendCodeButton.setOnClickListener(v -> {
            String phone = phoneInput.getText().toString().trim();
            viewModel.sendVerificationCode(phone);
        });

        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String code = verificationCodeInput.getText().toString().trim();

            LiveData<Boolean> result = viewModel.register(username, password, phone, code);
            if (result != null) {
                result.observe(this, success -> {
                    if (success) {
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        intent.putExtra("auto_login", true);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
} 