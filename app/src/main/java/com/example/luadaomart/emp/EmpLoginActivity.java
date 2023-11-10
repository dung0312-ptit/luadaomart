package com.example.luadaomart.emp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.man.ManHomeActivity;
import com.example.luadaomart.man.ManLoginActivity;
import com.example.luadaomart.model.Admin;
import com.example.luadaomart.model.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmpLoginActivity extends AppCompatActivity {

    private static final String TAG = "emp home";
    private TextView manText;
    private EditText usernameTxt,passTxt;
    private Button loginBtn;

    public static Employee employee;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_login);

        manText = findViewById(R.id.man_text);
        usernameTxt = findViewById(R.id.emp_login_user_text);
        passTxt = findViewById(R.id.emp_login_pass_text);
        loginBtn = findViewById(R.id.emp_login_btn);

        employee = new Employee();
        loginBtn.setOnClickListener(view -> {
            loginUser();
        });

        manText.setOnClickListener(view -> {
            Intent intent = new Intent(EmpLoginActivity.this, ManLoginActivity.class);
            startActivity(intent);
        });

    }
    private void loginUser() {
        String username = usernameTxt.getText().toString().trim();
        String password = passTxt.getText().toString().trim();

        if(username.equals("") || password.equals("")){
            Toast.makeText(EmpLoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
        }else{
            db.collection("employees")
                    .document(username)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.d(TAG,task.isSuccessful()+"");
                            if(task.isSuccessful()){
                                employee = task.getResult().toObject(Employee.class);
                                if(employee!=null && employee.getPassword().equals(password)){
                                    Intent intent = new Intent(EmpLoginActivity.this,EmpHomeActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(EmpLoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(EmpLoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }
}