package com.example.luadaomart.man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.inteface.GoodOrderItemOnclickListener;
import com.example.luadaomart.inteface.ManOrderListener;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.model.Order;
import com.example.luadaomart.model.OrderDetail;
import com.example.luadaomart.viewholder.GoodOderViewHolder;
import com.example.luadaomart.viewholder.ManOrderViewHolder;
import com.example.luadaomart.viewholder.OrderDetailViewHolder;
import com.example.luadaomart.viewholder.OrderViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManAllBillActivity extends AppCompatActivity {

    private Button searchBtn;
    private TextView sdTxt,edTxt;
    private SearchView searchView;
    private RecyclerView billRv;

    private RecyclerView billItemRv;
    private Button cancelBillBtn;
    private TextView billTotal,billPhone,billMethod,billEm,billDate;
    private RelativeLayout orderInforLayout;
    private String today;

    private Calendar start,end;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderCol;
    private FirestoreRecyclerAdapter<Order, ManOrderViewHolder> orderAdapter;
    private FirestoreRecyclerAdapter<OrderDetail, OrderDetailViewHolder> orderListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_all_bill);

        searchBtn = findViewById(R.id.man_bills_search_btn);
        sdTxt = findViewById(R.id.man_start_date);
        edTxt = findViewById(R.id.man_end_date);
        searchView = findViewById(R.id.man_bills_search);
        billRv = findViewById(R.id.man_bills_rv);

        cancelBillBtn = findViewById(R.id.man_bill_cancel);
        billItemRv = findViewById(R.id.man_bill_rv);
        billTotal = findViewById(R.id.man_bill_total);
        billDate = findViewById(R.id.man_bill_date);
        billEm = findViewById(R.id.man_bill_em);
        billMethod = findViewById(R.id.man_bill_method);
        billPhone = findViewById(R.id.man_bill_phone);
        orderInforLayout = findViewById(R.id.man_bill_infor_layout);

        orderCol = db.collection("orders");

        start= Calendar.getInstance();
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.HOUR_OF_DAY, 0);

        end= Calendar.getInstance();
        end.set(Calendar.HOUR, 23);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.HOUR_OF_DAY, 23);

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        today = formater.format(System.currentTimeMillis());

        sdTxt.setText(today);
        edTxt.setText(today);


        cancelBillBtn.setOnClickListener(view -> {
            orderInforLayout.setVisibility(View.GONE);
        });
        sdTxt.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    ManAllBillActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // on below line we are setting date to our edit text.
                            sdTxt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            start.set(year,monthOfYear,dayOfMonth,0,0,0);
                        }
                    },
                    // on below line we are passing year,
                    // month and day for selected date in our date picker.
                    year, month, day);
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show();
        });

        edTxt.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    ManAllBillActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // on below line we are setting date to our edit text.
                            edTxt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            end.set(year,monthOfYear,dayOfMonth,23,59,59);

                        }
                    },
                    // on below line we are passing year,
                    // month and day for selected date in our date picker.
                    year, month, day);
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show();
        });

        searchBtn.setOnClickListener(view -> {
            getList("");
        });
    }

    private void getList(String s) {
        Toast.makeText(ManAllBillActivity.this,start.getTime().toString()+"  "+end.getTime().toString(),Toast.LENGTH_LONG).show();
        Query query = orderCol.orderBy("timestamps", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("timestamps",start.getTime().getTime())
                .whereLessThanOrEqualTo("timestamps",end.getTime().getTime());

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        orderAdapter = new FirestoreRecyclerAdapter<Order, ManOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ManOrderViewHolder holder, int position, @NonNull Order model) {

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

//            @Override
//            public void onDataChanged() {
//                totalIncomeTxt.setText("Total Income: "+getTotal());
//                totalOrderTxt.setText("Total Orders: "+getItemCount());
//            }

            @NonNull
            @Override
            public ManOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.man_order_his_item, parent, false);

                return new ManOrderViewHolder(view);
            }
        };

        orderAdapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        billRv.setLayoutManager(layoutManager);
        billRv.setAdapter(orderAdapter);
    }

    private void showBill(Order model) {
        orderInforLayout.setVisibility(View.VISIBLE);
        Toast.makeText(ManAllBillActivity.this,"showbill",Toast.LENGTH_SHORT).show();
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
}