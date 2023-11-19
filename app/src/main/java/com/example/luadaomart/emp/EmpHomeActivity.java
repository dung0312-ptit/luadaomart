package com.example.luadaomart.emp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.luadaomart.R;
import com.example.luadaomart.inteface.EmpOrderListener;
import com.example.luadaomart.inteface.ManOrderListener;
import com.example.luadaomart.man.ManHomeActivity;
import com.example.luadaomart.man.ManLoginActivity;
import com.example.luadaomart.model.Employee;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.model.Order;
import com.example.luadaomart.model.OrderDetail;
import com.example.luadaomart.viewholder.GoodViewHolder;
import com.example.luadaomart.viewholder.OrderDetailViewHolder;
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

    private Button newOrderBtn,logoutBtn;
    private RecyclerView oderHisRv;
    private RelativeLayout orderInforLayout;
    private RecyclerView billItemRv;
    private Button cancelBillBtn;
    private TextView billTotal,billPhone,billMethod,billEm,billDate;
    private FirestoreRecyclerAdapter<OrderDetail, OrderDetailViewHolder> orderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);

        oderCol = db.collection("orders");

        employee = EmpLoginActivity.employee;

        newOrderBtn = findViewById(R.id.emp_home_new_order);
        oderHisRv = findViewById(R.id.emp_home_Oder_rv);
        logoutBtn = findViewById(R.id.emp_home_logout);
        orderInforLayout = findViewById(R.id.emp_bill_infor_layout);
        billItemRv = findViewById(R.id.emp_bill_rv);
        cancelBillBtn = findViewById(R.id.emp_bill_cancel);
        billTotal = findViewById(R.id.emp_bill_total);
        billPhone = findViewById(R.id.emp_bill_phone);
        billMethod = findViewById(R.id.emp_bill_method);
        billEm = findViewById(R.id.emp_bill_em);
        billDate = findViewById(R.id.emp_bill_date);


        cancelBillBtn.setOnClickListener(view -> {
            orderInforLayout.setVisibility(View.GONE);
        });
        newOrderBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EmpHomeActivity.this, NewOrderActivity.class);
            startActivity(intent);
        });
        logoutBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EmpHomeActivity.this, EmpLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
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

                holder.setListener(new EmpOrderListener() {
                    @Override
                    public void onEmpOrderListener(View view, int position) {
                        showBill(model);
                    }
                });

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

    private void showBill(Order model) {
        orderInforLayout.setVisibility(View.VISIBLE);
        billEm.setText(model.getEmployeeName());
        billTotal.setText(model.getTotalPrices()+"");
        billPhone.setText(model.getCusPhone());
        billMethod.setText(model.getMethod()==0?"Cash":"Credit card");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        billDate.setText(format.format(model.getTimestamps()));


        Query query = oderCol
                .document(model.getTimestamps()+"")
                .collection("detail");

        FirestoreRecyclerOptions<OrderDetail> options = new FirestoreRecyclerOptions.Builder<OrderDetail>()
                .setQuery(query, OrderDetail.class)
                .build();

        orderListAdapter = new FirestoreRecyclerAdapter<OrderDetail, OrderDetailViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position, @NonNull OrderDetail o) {
                holder.idTxt.setText(o.getId());
                holder.nameTxt.setText(o.getName());
                holder.priceTxt.setText(o.getPrice()+"");
                holder.amountTxt.setText(o.getAmount()+"");
                holder.totalTxt.setText(o.getTotal()+"");
            }
            @Override
            public int getItemCount() {
                return this.getSnapshots().size();
            }
            @NonNull
            @Override
            public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_detail_items, parent, false);

                return new OrderDetailViewHolder(view);
            }
        };

        orderListAdapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        billItemRv.setLayoutManager(layoutManager);
        billItemRv.setAdapter(orderListAdapter);
    }

    @Override
    public void onBackPressed() {

    }
}