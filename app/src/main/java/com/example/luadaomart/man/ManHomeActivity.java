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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.model.Admin;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.model.MonthStatistical;
import com.example.luadaomart.viewholder.GoodViewHolder;
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

import java.util.ArrayList;
import java.util.List;

public class ManHomeActivity extends AppCompatActivity {

    private static final String TAG = "man home";
    public static Admin admin;

    private Button addEmpBtn,storageBtn;
    private RecyclerView runningOutRv;
    private Spinner incomeSpin;
    private TableLayout tableLayout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference goodCol;
    private FirestoreRecyclerAdapter<Good, GoodViewHolder> runningOutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_home);

        admin = (Admin) getIntent().getSerializableExtra("user");
        goodCol = db.collection("goods");


        incomeSpin = findViewById(R.id.man_income_spin);
        addEmpBtn = findViewById(R.id.man_home_add_emp);
        storageBtn = findViewById(R.id.man_home_storage);
        runningOutRv = findViewById(R.id.man_home_running_rv);
        incomeSpin = findViewById(R.id.man_income_spin);
        tableLayout = findViewById(R.id.man_home_table);



        getRunningOutList();

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

        incomeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tableLayout.removeAllViews();
                Log.d(TAG,i+"");
                switch (i){
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

    private void staticByEmployee() {
        TableRow r0 = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        t1.setText("Employee");
        t2.setText("Income");
        r0.addView(t1);
        r0.addView(t2);

        tableLayout.addView(r0);
    }

    private void staticByDay() {
        TableRow r0 = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        t1.setText("Day");
        t2.setText("Income");
        r0.addView(t1);
        r0.addView(t2);

        tableLayout.addView(r0);
    }

    private void staticByMonth() {
        TableRow r0 = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        t1.setText("Month");
        t2.setText("Income");
        r0.addView(t1);
        r0.addView(t2);

        tableLayout.addView(r0);

        List<MonthStatistical> monthList = new ArrayList<>();
        db.collection("monthStatisticals").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot docum: task.getResult()){
                                MonthStatistical mon = docum.toObject(MonthStatistical.class);
                                monthList.add(mon);
                            }
                            for(MonthStatistical ms : monthList){
                                TableRow r = new TableRow(getBaseContext());
                                TextView tt1 = new TextView(getBaseContext());
                                TextView tt2 = new TextView(getBaseContext());
                                tt1.setText(ms.getMonth());
                                tt2.setText(ms.getTotal()+"");
                                r.addView(tt1);
                                r.addView(tt2);
                                tableLayout.addView(r);
                            }
                        }
                    }
                });


    }


    private void getRunningOutList() {
        Query query = goodCol
                .whereLessThanOrEqualTo("quantity",10)
                .orderBy("quantity");

        FirestoreRecyclerOptions<Good> options = new FirestoreRecyclerOptions.Builder<Good>()
                .setQuery(query, Good.class)
                .build();
        runningOutAdapter = new FirestoreRecyclerAdapter<Good, GoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GoodViewHolder holder, int position, @NonNull Good model) {
                holder.codeTxt.setText(model.getCode());
                holder.nameTxt.setText(model.getName());
                holder.priceTxt.setText(model.getPrice()+"");
                holder.quanTxt.setText(model.getQuantity()+"");
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