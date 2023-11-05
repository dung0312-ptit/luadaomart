package com.example.luadaomart.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luadaomart.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView idtxt,dateTxt,phoneTxt,totalTxt;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        idtxt =itemView.findViewById(R.id.order_his_id);
        dateTxt = itemView.findViewById(R.id.order_his_date);
        phoneTxt = itemView.findViewById(R.id.order_his_phone);
        totalTxt = itemView.findViewById(R.id.order_his_total);


    }
}
