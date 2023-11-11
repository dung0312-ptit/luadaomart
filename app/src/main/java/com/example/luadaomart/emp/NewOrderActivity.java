package com.example.luadaomart.emp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.adapter.GoodOrderAdapter;
import com.example.luadaomart.inteface.GoodOrderItemOnclickListener;
import com.example.luadaomart.model.DayStatistical;
import com.example.luadaomart.model.Employee;
import com.example.luadaomart.model.EmployeeStatistical;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.model.MonthStatistical;
import com.example.luadaomart.model.Order;
import com.example.luadaomart.model.OrderDetail;
import com.example.luadaomart.ultity.InvoiceGenerator;
import com.example.luadaomart.viewholder.GoodOderViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity implements GoodOrderAdapter.onDetailItemListener {

    private static final String TAG = "new order";
    private SearchView searchView;
    private RecyclerView searchRv, detailRv;
    private TextView totalTxt, dateTxt;
    private EditText phoneTxt;
    private Button cancelBtn, checkoutBtn;
    private Spinner methodSpin;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference goodCol, orderCol;
    private FirestoreRecyclerAdapter<Good, GoodOderViewHolder> searchSdapter;


    private Order order;
    private GoodOrderAdapter detailApdater;
    private DayStatistical ds;
    private MonthStatistical ms;
    private EmployeeStatistical es;
    private SimpleDateFormat dFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM");

    private Employee employee = EmpLoginActivity.employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        goodCol = db.collection("goods");
        orderCol = db.collection("orders");

        searchView = findViewById(R.id.order_search_good);
        searchRv = findViewById(R.id.order_good_rv);
        detailRv = findViewById(R.id.order_chose_rv);
        totalTxt = findViewById(R.id.order_total);
        phoneTxt = findViewById(R.id.order_phone_txt);
        dateTxt = findViewById(R.id.order_date);
        cancelBtn = findViewById(R.id.order_cancel);
        checkoutBtn = findViewById(R.id.order_submit);
        methodSpin = findViewById(R.id.order_method_spin);


        order = new Order(System.currentTimeMillis());
        //monthStatistic();
        checkMonthStatistic();
        checkDayStatistic();


        String dateString = dFormatter.format(new Date(order.getTimestamps()));
        dateTxt.setText(dateString);

        totalTxt.setText("0");

        LinearLayoutManager manage = new LinearLayoutManager(this);
        detailRv.setLayoutManager(manage);
        detailApdater = new GoodOrderAdapter(new ArrayList<>(), order.getTotalPrices());
        detailApdater.setListener(this);
        detailRv.setAdapter(detailApdater);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewOrderActivity.this, EmpHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRv.setVisibility(View.VISIBLE);
                getGoodList(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchRv.setVisibility(View.INVISIBLE);
                return false;
            }
        });


        checkoutBtn.setOnClickListener(view -> {
            List<OrderDetail> orderDetails = detailApdater.getList();
            order.setEmployeeId(EmpHomeActivity.employee.getId());
            order.setEmployeeName(EmpHomeActivity.employee.getName());
            order.setMethod(methodSpin.getSelectedItemPosition());

            String phone = phoneTxt.getText().toString().trim();
            if (phone.equals("")) phone += "no infor";
            order.setCusPhone(phone);
            int t = order.getTotalPrices();

            orderCol.document(order.getTimestamps() + "").set(order);
            for (OrderDetail od : orderDetails) {
                orderCol.document(order.getTimestamps() + "").collection("detail").document(od.getId()).set(od);
            }

            doStatisticAndInvoce(orderDetails);


            //finish();
        });

    }

    private void doStatisticAndInvoce(List<OrderDetail> orderDetails) {
        ds.setCount(ds.getCount() + 1);
        ds.setTotal(ds.getTotal() + order.getTotalPrices());
        ms.setCount(ms.getCount() + 1);
        ms.setTotal(ms.getTotal() + order.getTotalPrices());

        db.collection("monthStatisticals").document(ms.getMonth()).set(ms);
        db.collection("dayStatisticals").document(ds.getDay()).set(ds);
        db.collection("emStatisticals").document(employee.getId()).update("count",1);
        db.collection("emStatisticals").document(employee.getId()).update("total",order.getTotalPrices());

        for (OrderDetail od : orderDetails) {
            goodCol.document(od.getId()).update("quantity", FieldValue.increment( 0-od.getAmount()));
        }
        InvoiceGenerator invoice = new InvoiceGenerator(orderDetails, order);
        invoice.createInvoice();


        Intent intent = new Intent(NewOrderActivity.this, EmpHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void checkDayStatistic() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date(order.getTimestamps())).trim();
        Log.d(TAG, dateString);

        db.collection("dayStatisticals").document(dateString).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            ds = new DayStatistical(dateString, 0, 0);
                            db.collection("dayStatisticals").document(dateString).set(ds);
                        } else {
                            ds = documentSnapshot.toObject(DayStatistical.class);
                        }
                    }
                });
    }

    private void checkMonthStatistic() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String dateString = formatter.format(new Date(order.getTimestamps())).trim();
        Log.d(TAG, dateString);

        db.collection("monthStatisticals").document(dateString).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            ms = new MonthStatistical(dateString, 0, 0);
                            db.collection("monthStatisticals").document(dateString).set(ms);
                        } else {
                            ms = documentSnapshot.toObject(MonthStatistical.class);
                        }
                    }
                });
    }


    private void getGoodList(String s) {
        Query query = goodCol.orderBy("code").startAt(s).endAt(s + "\uf8ff");

        FirestoreRecyclerOptions<Good> options = new FirestoreRecyclerOptions.Builder<Good>()
                .setQuery(query, Good.class)
                .build();
        searchSdapter = new FirestoreRecyclerAdapter<Good, GoodOderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GoodOderViewHolder holder, int position, @NonNull Good model) {
                holder.codeTxt.setText(model.getCode());
                holder.nameTxt.setText(model.getName());
                holder.priceTxt.setText(model.getPrice() + "");
                holder.quanTxt.setText(model.getQuantity() + "");
                holder.configLayout.setVisibility(View.INVISIBLE);

                holder.setListener(new GoodOrderItemOnclickListener() {
                    @Override
                    public void onGoodOrderItemOnclickListener(View view, int position) {
                        choseGood(model);
                        searchRv.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return this.getSnapshots().size();
            }

            @NonNull
            @Override
            public GoodOderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.good_item, parent, false);

                return new GoodOderViewHolder(view);
            }
        };

        searchSdapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchRv.setLayoutManager(layoutManager);
        searchRv.setAdapter(searchSdapter);
    }

    private void choseGood(Good model) {

        if(model.getQuantity()<=0){
            Toast.makeText(NewOrderActivity.this,"This item is running out",Toast.LENGTH_SHORT).show();
            return;
        }
        OrderDetail orderDetail = new OrderDetail(model.getCode(), model.getName(), model.getPrice(), 1,model.getQuantity(), model.getPrice());
        order.setTotalPrices(order.getTotalPrices() + model.getPrice());
        detailApdater.setTotal(order.getTotalPrices());
        detailApdater.list.add(orderDetail);
        totalTxt.setText(order.getTotalPrices() + "");
        detailApdater.notifyDataSetChanged();
    }


    @Override
    public void newTotal(int total) {
        order.setTotalPrices(total);
        totalTxt.setText(total + "");
    }
}