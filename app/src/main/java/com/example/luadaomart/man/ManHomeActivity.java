package com.example.luadaomart.man;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.luadaomart.R;
import com.example.luadaomart.model.Admin;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManHomeActivity extends AppCompatActivity {

    public static Admin admin;

    private Button addEmpBtn,storageBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_home);

        admin = (Admin) getIntent().getSerializableExtra("user");


        addEmpBtn = findViewById(R.id.man_home_add_emp);
        storageBtn = findViewById(R.id.man_home_storage);

        addEmpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ManHomeActivity.this, ManageEmpActivity.class);
            intent.putExtra("user",admin);
            startActivity(intent);
        });

        storageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ManHomeActivity.this, ManageStorageActivity.class);
            intent.putExtra("user",admin);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}