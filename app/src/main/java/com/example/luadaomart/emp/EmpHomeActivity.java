package com.example.luadaomart.emp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.luadaomart.R;
import com.example.luadaomart.model.Employee;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.model.Order;
import com.example.luadaomart.viewholder.GoodViewHolder;
import com.example.luadaomart.viewholder.OrderViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmpHomeActivity extends AppCompatActivity {

    public static Employee employee;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference oderCol;
    private FirestoreRecyclerAdapter<Order, OrderViewHolder> orderHistoryAdapter;

    private Button newOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);

        employee = (Employee) getIntent().getSerializableExtra("user");

        newOrderBtn = findViewById(R.id.emp_home_new_order);


        newOrderBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EmpHomeActivity.this, NewOrderActivity.class);
            startActivity(intent);
        });
    }
}