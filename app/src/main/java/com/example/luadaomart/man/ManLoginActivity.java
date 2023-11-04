package com.example.luadaomart.man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.emp.EmpLoginActivity;
import com.example.luadaomart.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class ManLoginActivity extends AppCompatActivity {

    private EditText userTxt, passTxt;
    private Button loginBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_login);

        userTxt = findViewById(R.id.user_text);
        passTxt = findViewById(R.id.pass_text);
        loginBtn = findViewById(R.id.man_login_btn);

        loginBtn.setOnClickListener(view -> {
            loginUser();
        });


    }

    private void loginUser() {
        String username = userTxt.getText().toString().trim();
        String password = passTxt.getText().toString().trim();

        if(username.equals("") || password.equals("")){
            Toast.makeText(ManLoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
        }else{
            db.collection("admins")
                    .document(username)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                Admin admin = task.getResult().toObject(Admin.class);
                                if(admin!=null && admin.getPassword().equals(password)){
                                    Intent intent = new Intent(ManLoginActivity.this, ManHomeActivity.class);
                                    intent.putExtra("user",admin);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(ManLoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(ManLoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }
}