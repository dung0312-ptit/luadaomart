package com.example.luadaomart.man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.model.Admin;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.viewholder.GoodViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManHomeActivity extends AppCompatActivity {

    public static Admin admin;

    private Button addEmpBtn,storageBtn;
    private RecyclerView runningOutRv;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference goodCol;
    private FirestoreRecyclerAdapter<Good, GoodViewHolder> runningOutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_home);

        admin = (Admin) getIntent().getSerializableExtra("user");
        goodCol = db.collection("goods");


        addEmpBtn = findViewById(R.id.man_home_add_emp);
        storageBtn = findViewById(R.id.man_home_storage);
        runningOutRv = findViewById(R.id.man_home_running_rv);

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