package com.example.luadaomart.man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.model.Admin;
import com.example.luadaomart.model.Employee;
import com.example.luadaomart.viewholder.EmpViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ManageEmpActivity extends AppCompatActivity {

    private static final String TAG = "emp management";
    private Admin admin = ManHomeActivity.admin;

    private RelativeLayout addEmpLayout;
    private Button addEmpBtn,cancelAddBtn,submitAddBtn;
    private TextView empIdTxt;
    private EditText empNametxt,empPassTxt;
    private RecyclerView empRV;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<Employee, EmpViewHolder> adapter;

    private CollectionReference empCol;


    private long count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_emp);

        addEmpLayout = findViewById(R.id.man_emp_add_layout);
        addEmpBtn = findViewById(R.id.man_add_emp);
        cancelAddBtn = findViewById(R.id.man_cancel_add_emp);
        submitAddBtn = findViewById(R.id.man_submit_add_emp);
        empIdTxt = findViewById(R.id.man_emp_id);
        empNametxt = findViewById(R.id.man_emp_name);
        empPassTxt = findViewById(R.id.man_emp_pass);
        empRV = findViewById(R.id.man_emp_rv);

        empCol = db.collection("employees");


        getEmpList();




        addEmpBtn.setOnClickListener(view -> {
            addEmpLayout.setVisibility(View.VISIBLE);

            count = System.currentTimeMillis();
            empIdTxt.setText(count+"");
        });

        cancelAddBtn.setOnClickListener(view -> {
            unvisibleEmLayout();

        });

        submitAddBtn.setOnClickListener(view -> {
            Log.d(TAG,"sumit clicked");
            String name = empNametxt.getText().toString().trim();
            String pass = empPassTxt.getText().toString().trim();

            if(name.equals("") || pass.equals("")){
                Toast.makeText(ManageEmpActivity.this,"Fill the form please", Toast.LENGTH_SHORT).show();
            }else{
                Employee em = new Employee(count+"",name,pass);
                empCol.document(count+"").set(em);
                Toast.makeText(ManageEmpActivity.this,"Added "+em.getName(), Toast.LENGTH_SHORT).show();
                unvisibleEmLayout();
            }

        });


    }

    private void getEmpList() {
        Query query = empCol.orderBy("id");

        FirestoreRecyclerOptions<Employee> options = new FirestoreRecyclerOptions.Builder<Employee>()
                .setQuery(query,Employee.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Employee, EmpViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EmpViewHolder holder, int position, @NonNull Employee model) {
                holder.idTxt.setText(model.getId());
                holder.nameTxt.setText(model.getName());
                holder.editBtn.setOnClickListener(view -> {
                    holder.editLayout.setVisibility(View.VISIBLE);
                    holder.newPassTxt.setText("");
                });

                holder.submitBtn.setOnClickListener(view -> {
                    String pass = holder.newPassTxt.getText().toString().trim();
                    if(pass.equals("")){
                        Toast.makeText(ManageEmpActivity.this,"Insert new password please",Toast.LENGTH_SHORT).show();
                    }else{
                        empCol.document(model.getId()).update("password",pass);
                        holder.editLayout.setVisibility(View.GONE);
                        Toast.makeText(ManageEmpActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                    }

                });

                holder.cancelBtn.setOnClickListener(view -> {
                    holder.editLayout.setVisibility(View.GONE);
                });

                holder.deleteBtn.setOnClickListener(view -> {
                    empCol.document(model.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ManageEmpActivity.this,"Deteted",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ManageEmpActivity.this,"Connection error",Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            }
            @Override
            public int getItemCount() {
                return this.getSnapshots().size();
            }
            @NonNull
            @Override
            public EmpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.emp_item, parent, false);

                return new EmpViewHolder(view);
            }
        };

        adapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        empRV.setLayoutManager(layoutManager);
        empRV.setAdapter(adapter);
    }

    private void unvisibleEmLayout() {
        addEmpLayout.setVisibility(View.GONE);
        empNametxt.setText("");
        empPassTxt.setText("");
    }
}