package com.example.luadaomart.emp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.luadaomart.R;
import com.example.luadaomart.model.Employee;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.model.Order;
import com.example.luadaomart.viewholder.GoodViewHolder;
import com.example.luadaomart.viewholder.OrderViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmpHomeActivity extends AppCompatActivity {

    private static final String TAG = "emp home";
    public static Employee employee;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference oderCol;
    private FirestoreRecyclerAdapter<Order, OrderViewHolder> orderHistoryAdapter;

    private Button newOrderBtn;
    private RecyclerView oderHisRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);

        oderCol = db.collection("orders");

        employee = EmpLoginActivity.employee;

        newOrderBtn = findViewById(R.id.emp_home_new_order);
        oderHisRv = findViewById(R.id.emp_home_Oder_rv);


        newOrderBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EmpHomeActivity.this, NewOrderActivity.class);
            startActivity(intent);
        });

        showOrderHis();
    }

    private void showOrderHis() {
        //Log.d(TAG,employee.getId());
        Query query = oderCol
                .whereEqualTo("employeeId",employee.getId())
                .orderBy("timestamps", Query.Direction.DESCENDING)
                .limit(15);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        orderHistoryAdapter = new FirestoreRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {

                Log.d(TAG,position+"");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String dateString = formatter.format(new Date(model.getTimestamps()));


                holder.idtxt.setText(model.getTimestamps()+"");
                holder.dateTxt.setText(dateString);
                holder.phoneTxt.setText(model.getCusPhone());
                holder.totalTxt.setText(model.getTotalPrices()+"");


            }
            @Override
            public int getItemCount() {
                return this.getSnapshots().size();
            }
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_his_item, parent, false);

                return new OrderViewHolder(view);
            }
        };

        orderHistoryAdapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        oderHisRv.setLayoutManager(layoutManager);
        oderHisRv.setAdapter(orderHistoryAdapter);

    }

    @Override
    public void onBackPressed() {

    }
}