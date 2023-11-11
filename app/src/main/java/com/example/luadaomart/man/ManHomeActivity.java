package com.example.luadaomart.man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.inteface.ManOrderListener;
import com.example.luadaomart.model.Admin;
import com.example.luadaomart.model.DayStatistical;
import com.example.luadaomart.model.EmployeeStatistical;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.model.MonthStatistical;
import com.example.luadaomart.model.Order;
import com.example.luadaomart.model.OrderDetail;
import com.example.luadaomart.viewholder.GoodOderViewHolder;
import com.example.luadaomart.viewholder.GoodViewHolder;
import com.example.luadaomart.viewholder.ManOrderViewHolder;
import com.example.luadaomart.viewholder.OrderDetailViewHolder;
import com.example.luadaomart.viewholder.OrderViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManHomeActivity extends AppCompatActivity {

    private static final String TAG = "man home";
    public static Admin admin;

    private int total,cash,credit;
    private Button addEmpBtn, storageBtn,endDayBtn;
    private RecyclerView runningOutRv, orderHisRv;
    private Spinner incomeSpin;
    private TableLayout tableLayout;
    private TextView totalIncomeTxt, totalOrderTxt;
    private RelativeLayout orderInforLayout,endDayLayout;
    private RecyclerView billItemRv;
    private Button cancelBillBtn,cancelEndDayBtn;
    private TextView billTotal,billPhone,billMethod,billEm,billDate;
    private TextView countTxt,creditTxt,cashTxt,endTotalTxt;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference goodCol, orderCol;
    private FirestoreRecyclerAdapter<Good, GoodViewHolder> runningOutAdapter;
    private FirestoreRecyclerAdapter<Order, ManOrderViewHolder> orderHisAdapter;
    private FirestoreRecyclerAdapter<OrderDetail, OrderDetailViewHolder> orderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_home);

        admin = (Admin) getIntent().getSerializableExtra("user");
        goodCol = db.collection("goods");
        orderCol = db.collection("orders");

        incomeSpin = findViewById(R.id.man_income_spin);
        addEmpBtn = findViewById(R.id.man_home_add_emp);
        storageBtn = findViewById(R.id.man_home_storage);
        runningOutRv = findViewById(R.id.man_home_running_rv);
        incomeSpin = findViewById(R.id.man_income_spin);
        tableLayout = findViewById(R.id.man_home_table);
        orderHisRv = findViewById(R.id.man_home_today_oders);
        totalIncomeTxt = findViewById(R.id.man_home_today_income_txt);
        totalOrderTxt = findViewById(R.id.man_home_today_ord_txt);
        orderInforLayout = findViewById(R.id.man_bill_infor_layout);
        cancelBillBtn = findViewById(R.id.man_bill_cancel);
        billItemRv = findViewById(R.id.man_bill_rv);
        billTotal = findViewById(R.id.man_bill_total);
        billDate = findViewById(R.id.man_bill_date);
        billEm = findViewById(R.id.man_bill_em);
        billMethod = findViewById(R.id.man_bill_method);
        billPhone = findViewById(R.id.man_bill_phone);
        endDayLayout = findViewById(R.id.man_end_day_layout);
        endDayBtn = findViewById(R.id.man_home_end_day);
        cancelEndDayBtn = findViewById(R.id.man_end_day_cancel);
        countTxt = findViewById(R.id.man_endday_count);
        creditTxt = findViewById(R.id.man_endday_credit);
        cashTxt = findViewById(R.id.man_endday_cash);
        endTotalTxt = findViewById(R.id.man_endday_total);




        getRunningOutList();
        getToday();

        endDayBtn.setOnClickListener(view -> {
            endDayLayout.setVisibility(View.VISIBLE);
            getEndDay();
        });

        cancelEndDayBtn.setOnClickListener(view -> {
            endDayLayout.setVisibility(View.GONE);
        });

        cancelBillBtn.setOnClickListener(view -> {
            orderInforLayout.setVisibility(View.GONE);
        });

        addEmpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ManHomeActivity.this, ManageEmpActivity.class);
            intent.putExtra("user", admin);
            startActivity(intent);
        });

        storageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ManHomeActivity.this, ManageStorageActivity.class);
            intent.putExtra("user", admin);
            startActivity(intent);
        });

        incomeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tableLayout.removeAllViews();
                Log.d(TAG, i + "");
                switch (i) {
                    case 0:
                        staticByMonth();
                        break;
                    case 1:
                        staticByDay();
                        break;
                    case 2:
                        staticByEmployee();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getEndDay() {
        cash = 0;
        credit = 0;
        List<Order> orders = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        now.getTime().getTime();
        Toast.makeText(ManHomeActivity.this,now.getTime().getTime()+" "+now.getTime().toString(),Toast.LENGTH_LONG).show();
        orderCol.whereGreaterThanOrEqualTo("timestamps",now.getTime().getTime())
                .orderBy("timestamps", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot d : task.getResult()){
                                orders.add(d.toObject(Order.class));
                            }
                            for(Order o: orders){
                                if(o.getMethod()==0)cash+=o.getTotalPrices();
                                else credit +=o.getTotalPrices();
                            }
                            countTxt.setText(orders.size()+"");
                            creditTxt.setText(credit+"");
                            cashTxt.setText(cash+"");
                            endTotalTxt.setText((credit+cash)+"");

                        }
                    }
                });
    }

    private void getToday() {

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        now.getTime().getTime();
        Toast.makeText(ManHomeActivity.this,now.getTime().getTime()+" "+now.getTime().toString(),Toast.LENGTH_LONG).show();
        Query query = orderCol
                .whereGreaterThanOrEqualTo("timestamps",now.getTime().getTime())
                .orderBy("timestamps", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        orderHisAdapter = new FirestoreRecyclerAdapter<Order, ManOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ManOrderViewHolder holder, int position, @NonNull Order model) {

                Log.d(TAG,position+"");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String dateString = formatter.format(new Date(model.getTimestamps()));


                holder.idtxt.setText(model.getTimestamps()+"");
                holder.dateTxt.setText(dateString);
                holder.emTxt.setText(model.getEmployeeName());
                holder.totalTxt.setText(model.getTotalPrices()+"");

                holder.setListener(new ManOrderListener() {
                    @Override
                    public void onManOrderListener(View view, int position) {
                        showBill(model);
                    }
                });
            }
            @Override
            public int getItemCount() {
                return this.getSnapshots().size();
            }

            public int getTotal(){
                int sum = 0;
                for (Order o: this.getSnapshots()){
                    sum += o.getTotalPrices();
                }
                return sum;
            };

            @Override
            public void onDataChanged() {
                totalIncomeTxt.setText("Total Income: "+getTotal());
                totalOrderTxt.setText("Total Orders: "+getItemCount());
            }

            @NonNull
            @Override
            public ManOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.man_order_his_item, parent, false);

                return new ManOrderViewHolder(view);
            }
        };

        orderHisAdapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        orderHisRv.setLayoutManager(layoutManager);
        orderHisRv.setAdapter(orderHisAdapter);







    }

    private void showBill(Order model) {
        orderInforLayout.setVisibility(View.VISIBLE);
        billEm.setText(model.getEmployeeName());
        billTotal.setText(model.getTotalPrices()+"");
        billPhone.setText(model.getCusPhone());
        billMethod.setText(model.getMethod()==0?"Cash":"Credit card");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        billDate.setText(format.format(model.getTimestamps()));


        Query query = orderCol
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

    private void staticByEmployee() {
        TableRow r0 = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        TextView t3 = new TextView(this);
        t1.setText("EmID");
        t2.setText("Bill Count");
        t3.setText("Income");
        r0.addView(t1);
        r0.addView(t2);
        r0.addView(t3);

        tableLayout.addView(r0);

        List<EmployeeStatistical> emList = new ArrayList<>();
        db.collection("emStatisticals").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot docum : task.getResult()) {
                                EmployeeStatistical day = docum.toObject(EmployeeStatistical.class);
                                emList.add(day);
                            }
                            for (EmployeeStatistical es : emList) {
                                TableRow r = new TableRow(getBaseContext());
                                TextView tt1 = new TextView(getBaseContext());
                                TextView tt2 = new TextView(getBaseContext());
                                TextView tt3 = new TextView(getBaseContext());
                                tt1.setText(es.getEmID());
                                tt2.setText(es.getCount() + "");
                                tt3.setText(es.getTotal() + "");
                                r.addView(tt1);
                                r.addView(tt2);
                                r.addView(tt3);

                                r.setBackgroundResource(R.drawable.border_item);
                                tableLayout.addView(r);
                            }
                        }
                    }
                });

    }

    private void staticByDay() {
        TableRow r0 = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        TextView t3 = new TextView(this);
        t1.setText("Day");
        t2.setText("Bill Count");
        t3.setText("Income");
        r0.addView(t1);
        r0.addView(t2);
        r0.addView(t3);

        tableLayout.addView(r0);

        List<DayStatistical> dayList = new ArrayList<>();
        db.collection("dayStatisticals").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot docum : task.getResult()) {
                                DayStatistical day = docum.toObject(DayStatistical.class);
                                dayList.add(day);
                            }
                            for (DayStatistical ds : dayList) {
                                TableRow r = new TableRow(getBaseContext());
                                TextView tt1 = new TextView(getBaseContext());
                                TextView tt2 = new TextView(getBaseContext());
                                TextView tt3 = new TextView(getBaseContext());
                                tt1.setText(ds.getDay());
                                tt2.setText(ds.getCount() + "");
                                tt3.setText(ds.getTotal() + "");
                                r.addView(tt1);
                                r.addView(tt2);
                                r.addView(tt3);
                                r.setBackgroundResource(R.drawable.border_item);
                                tableLayout.addView(r);
                            }
                        }
                    }
                });

    }

    private void staticByMonth() {
        TableRow r0 = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        TextView t3 = new TextView(this);
        t1.setText("Month");
        t2.setText("Bill Count");
        t3.setText("Income");
        r0.addView(t1);
        r0.addView(t2);
        r0.addView(t3);


        tableLayout.addView(r0);

        List<MonthStatistical> monthList = new ArrayList<>();
        db.collection("monthStatisticals").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot docum : task.getResult()) {
                                MonthStatistical mon = docum.toObject(MonthStatistical.class);
                                monthList.add(mon);
                            }
                            for (MonthStatistical ms : monthList) {
                                TableRow r = new TableRow(getBaseContext());
                                TextView tt1 = new TextView(getBaseContext());
                                TextView tt2 = new TextView(getBaseContext());
                                TextView tt3 = new TextView(getBaseContext());
                                tt1.setText(ms.getMonth());
                                tt2.setText(ms.getCount() + "");
                                tt3.setText(ms.getTotal() + "");
                                r.addView(tt1);
                                r.addView(tt2);
                                r.addView(tt3);

                                r.setBackgroundResource(R.drawable.border_item);
                                tableLayout.addView(r);
                            }
                        }
                    }
                });


    }


    private void getRunningOutList() {
        Query query = goodCol
                .whereLessThanOrEqualTo("quantity", 10)
                .orderBy("quantity");

        FirestoreRecyclerOptions<Good> options = new FirestoreRecyclerOptions.Builder<Good>()
                .setQuery(query, Good.class)
                .build();
        runningOutAdapter = new FirestoreRecyclerAdapter<Good, GoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GoodViewHolder holder, int position, @NonNull Good model) {
                holder.codeTxt.setText(model.getCode());
                holder.nameTxt.setText(model.getName());
                holder.priceTxt.setText(model.getPrice() + "");
                holder.quanTxt.setText(model.getQuantity() + "");
                holder.configLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public int getItemCount() {
                return this.getSnapshots().size();
            }

            @NonNull
            @Override
            public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.good_item, parent, false);

                return new GoodViewHolder(view);
            }
        };

        runningOutAdapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        runningOutRv.setLayoutManager(layoutManager);
        runningOutRv.setAdapter(runningOutAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}