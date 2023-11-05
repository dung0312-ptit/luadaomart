package com.example.luadaomart.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luadaomart.R;

public class EmpViewHolder extends RecyclerView.ViewHolder {


    public TextView idTxt,nameTxt;
    public EditText newPassTxt;
    public Button editBtn,deleteBtn,cancelBtn,submitBtn;
    public LinearLayout editLayout;
    public EmpViewHolder(@NonNull View itemView) {
        super(itemView);

        idTxt = itemView.findViewById(R.id.emp_item_id);
        nameTxt = itemView.findViewById(R.id.emp_item_name);
        editBtn = itemView.findViewById(R.id.emp_item_edit);
        deleteBtn = itemView.findViewById(R.id.emp_item_delete);
        newPassTxt = itemView.findViewById(R.id.emp_item_new_pass);
        cancelBtn = itemView.findViewById(R.id.emp_item_cancel);
        submitBtn = itemView.findViewById(R.id.emp_item_submit);
        editLayout = itemView.findViewById(R.id.emp_item_edit_layout);
    }
}
