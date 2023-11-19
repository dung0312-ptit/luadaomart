package com.example.luadaomart.man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.luadaomart.R;
import com.example.luadaomart.model.Employee;
import com.example.luadaomart.model.Good;
import com.example.luadaomart.viewholder.EmpViewHolder;
import com.example.luadaomart.viewholder.GoodViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageStorageActivity extends AppCompatActivity {


    private static final String TAG = "manage storage";
    private Button addBtn, cancelBtn, submitBtn;
    private SearchView searchView;
    private EditText codeTxt, nameTxt, priceTxt, quantityTxt;
    private RecyclerView goodRv;
    private RelativeLayout addGoodLayout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference goodCol;
    private FirestoreRecyclerAdapter<Good, GoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_storage);

        addBtn = findViewById(R.id.man_add_good);
        cancelBtn = findViewById(R.id.man_cancel_add_good);
        submitBtn = findViewById(R.id.man_submit_add_good);
        searchView = findViewById(R.id.man_search_good);
        codeTxt = findViewById(R.id.man_good_code);
        nameTxt = findViewById(R.id.man_good_name);
        priceTxt = findViewById(R.id.man_good_price);
        quantityTxt = findViewById(R.id.man_good_quantity);
        goodRv = findViewById(R.id.man_good_rv);
        addGoodLayout = findViewById(R.id.man_good_add_layout);

        goodCol = db.collection("goods");


        getGoodList("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getGoodList(newText);
                return true;
            }
        });

        addBtn.setOnClickListener(view -> {
            addGoodLayout.setVisibility(View.VISIBLE);
            codeTxt.setText("");
            nameTxt.setText("");
            priceTxt.setText("");
            quantityTxt.setText("");

        });

        cancelBtn.setOnClickListener(view -> {

            addGoodLayout.setVisibility(View.GONE);
        });

        submitBtn.setOnClickListener(view -> {
            Log.d(TAG, "sumit clicked");
            String code = codeTxt.getText().toString().trim();
            String name = nameTxt.getText().toString().trim();
            try{
                int price = Integer.parseInt(priceTxt.getText().toString().trim());
                int quantity = Integer.parseInt(quantityTxt.getText().toString().trim());
                if (name.equals("") || code.equals("") || price<0 || quantity<0 ) {
                    Toast.makeText(ManageStorageActivity.this, "Fill the form please", Toast.LENGTH_SHORT).show();
                } else {
                    Good g = new Good(code,name,price,quantity);
                    goodCol.document(g.getCode()).set(g);
                    Toast.makeText(ManageStorageActivity.this, "Added " + g.getCode() + " "+g.getName(), Toast.LENGTH_SHORT).show();
                    addGoodLayout.setVisibility(View.GONE);
                }
            }catch (Exception e){
                Toast.makeText(ManageStorageActivity.this, "Fill the form please", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getGoodList(String s) {
        Query query = goodCol.orderBy("name").startAt(s).endAt(s+"\uf8ff");

        FirestoreRecyclerOptions<Good> options = new FirestoreRecyclerOptions.Builder<Good>()
                .setQuery(query, Good.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Good, GoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GoodViewHolder holder, int position, @NonNull Good model) {
                holder.codeTxt.setText(model.getCode());
                holder.nameTxt.setText(model.getName());
                holder.priceTxt.setText(model.getPrice()+"");
                holder.quanTxt.setText(model.getQuantity()+"");
                holder.addBtn.setOnClickListener(view -> {
                    holder.addLayout.setVisibility(View.VISIBLE);
                    holder.amountTxt.setText("");
                });

                holder.submitBtn.setOnClickListener(view -> {
                   try{
                       int amount = Integer.parseInt(holder.amountTxt.getText().toString().trim());
                       if(amount<0){
                           Toast.makeText(ManageStorageActivity.this,"add amount need to be over than 0",Toast.LENGTH_SHORT).show();
                       }else{
                           goodCol.document(model.getCode()).update("quantity",model.getQuantity()+amount);
                           holder.addLayout.setVisibility(View.GONE);
                           Toast.makeText(ManageStorageActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                       }
                   }catch (Exception e){
                       Toast.makeText(ManageStorageActivity.this,"incorrect amount number",Toast.LENGTH_SHORT).show();
                   }


                });

                holder.cancelBtn.setOnClickListener(view -> {
                    holder.addLayout.setVisibility(View.GONE);
                });

                holder.deleteBtn.setOnClickListener(view -> {
                    if(model.getQuantity()>0){
                        Toast.makeText(ManageStorageActivity.this,"quantity > 0, can not delete",Toast.LENGTH_SHORT).show();
                    }else{
                        goodCol.document(model.getCode())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ManageStorageActivity.this,"Deteted",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ManageStorageActivity.this,"Connection error",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                });
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

        adapter.startListening();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        goodRv.setLayoutManager(layoutManager);
        goodRv.setAdapter(adapter);
    }
}