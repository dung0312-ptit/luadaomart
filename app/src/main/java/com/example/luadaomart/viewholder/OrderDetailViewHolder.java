package com.example.luadaomart.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luadaomart.R;

public class OrderDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView idTxt,nameTxt,priceTxt,totalTxt,amountTxt;
    public EditText editAmountTxt;
    public LinearLayout editLayout;
    public Button cancelBtn,submitBtn;



    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        idTxt = itemView.findViewById(R.id.detail_id);
        nameTxt = itemView.findViewById(R.id.detail_name);
        priceTxt = itemView.findViewById(R.id.detail_price);
        totalTxt = itemView.findViewById(R.id.detail_total);
        amountTxt = itemView.findViewById(R.id.detail_quan);
        editAmountTxt = itemView.findViewById(R.id.detail_item_amount);
        editLayout = itemView.findViewById(R.id.detail_edit_layout);
        cancelBtn = itemView.findViewById(R.id.detail_item_cancel);
        submitBtn = itemView.findViewById(R.id.detail_item_submit);
    }
}
