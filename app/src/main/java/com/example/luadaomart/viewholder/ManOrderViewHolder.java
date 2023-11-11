package com.example.luadaomart.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luadaomart.R;
import com.example.luadaomart.inteface.GoodOrderItemOnclickListener;
import com.example.luadaomart.inteface.ManOrderListener;

public class ManOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView idtxt,dateTxt,emTxt,totalTxt;
    public ManOrderListener listener;

    public ManOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        idtxt =itemView.findViewById(R.id.man_order_his_id);
        dateTxt = itemView.findViewById(R.id.man_order_his_date);
        emTxt = itemView.findViewById(R.id.man_order_his_em);
        totalTxt = itemView.findViewById(R.id.man_order_his_total);
        itemView.setOnClickListener(this);

    }

    public void setListener(ManOrderListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onManOrderListener(view,getAdapterPosition());
    }
}
