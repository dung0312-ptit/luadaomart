package com.example.luadaomart.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luadaomart.R;
import com.example.luadaomart.model.OrderDetail;
import com.example.luadaomart.viewholder.OrderDetailViewHolder;

import java.util.List;

public class GoodOrderAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> {
    private static final String TAG = "new order";
    public List<OrderDetail> list;
    private onDetailItemListener listener;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setListener(onDetailItemListener listener) {
        this.listener = listener;
    }

    public List<OrderDetail> getList() {
        return list;
    }

    public void setList(List<OrderDetail> list) {
        this.list = list;

    }

    public GoodOrderAdapter(List<OrderDetail> list, int totalPrices) {
        this.list = list;
        this.total=total;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_items, parent, false);

        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        Log.d(TAG,position+" "+list.size());
        OrderDetail o = list.get(position);
        Log.d(TAG,o.getId()+ " "+o.getName()+" "+o.getPrice()+" "+o.getAmount()+" "+o.getTotal());
        holder.idTxt.setText(o.getId());
        holder.nameTxt.setText(o.getName());
        holder.priceTxt.setText(o.getPrice()+"");
        holder.amountTxt.setText(o.getAmount()+"");
        holder.totalTxt.setText(o.getTotal()+"");

        holder.amountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.editLayout.setVisibility(View.VISIBLE);
                holder.editAmountTxt.setText("");
            }
        });

        holder.submitBtn.setOnClickListener(view -> {
            try{
                int newA = Integer.parseInt(holder.editAmountTxt.getText().toString().trim());
                if(newA>0){
                    if(newA>o.getMax())newA = o.getMax();

                        total =total-o.getPrice()*o.getAmount() + o.getPrice()*newA;
                        o.setAmount(newA);
                        o.setTotal(newA*o.getPrice());
                        list.set(position,o);


                }else{
                    total = total - o.getPrice()*o.getAmount();
                    list.remove(position);
                }
                holder.editLayout.setVisibility(View.GONE);
                listener.newTotal(total);
                notifyDataSetChanged();
            }catch (Exception e){
                holder.editLayout.setVisibility(View.GONE);
            }


        });

        holder.cancelBtn.setOnClickListener(view -> {
            holder.editLayout.setVisibility(View.GONE);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onDetailItemListener{
        public void newTotal(int total);
    }
}
