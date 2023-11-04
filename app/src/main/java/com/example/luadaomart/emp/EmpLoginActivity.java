package com.example.luadaomart.emp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.luadaomart.R;
import com.example.luadaomart.man.ManLoginActivity;

public class EmpLoginActivity extends AppCompatActivity {

    private TextView manText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_login);

        manText = findViewById(R.id.man_text);

        manText.setOnClickListener(view -> {
            Intent intent = new Intent(EmpLoginActivity.this, ManLoginActivity.class);
            startActivity(intent);
        });

    }
}