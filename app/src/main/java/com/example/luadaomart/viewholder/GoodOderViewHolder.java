package com.example.luadaomart.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luadaomart.R;
import com.example.luadaomart.inteface.GoodOrderItemOnclickListener;

public class GoodOderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView codeTxt,nameTxt,priceTxt,quanTxt;
    public Button addBtn,deleteBtn,cancelBtn,submitBtn;
    public EditText amountTxt;
    public LinearLayout addLayout,configLayout;

    public GoodOrderItemOnclickListener listener;

   public GoodOderViewHolder(@NonNull View itemView) {
        super(itemView);

        codeTxt = itemView.findViewById(R.id.good_item_id);
        nameTxt = itemView.findViewById(R.id.goode_item_name);
        priceTxt = itemView.findViewById(R.id.goode_item_price);
        quanTxt = itemView.findViewById(R.id.goode_item_quan);
        addBtn = itemView.findViewById(R.id.good_item_add);
        deleteBtn = itemView.findViewById(R.id.good_item_delete);
        cancelBtn = itemView.findViewById(R.id.good_item_cancel);
        submitBtn = itemView.findViewById(R.id.good_item_submit);
        amountTxt = itemView.findViewById(R.id.good_item_amount);
        addLayout = itemView.findViewById(R.id.good_item_edit_layout);
        configLayout = itemView.findViewById(R.id.good_item_config_layout);
        itemView.setOnClickListener(this);
    }

    public void setListener(GoodOrderItemOnclickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onGoodOrderItemOnclickListener(view,getAdapterPosition());
    }
}
