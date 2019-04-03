package com.example.heramb.know_your_govt;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    public TextView postofofficial;
    public TextView nameofofficial;


    public OfficialViewHolder(View itemView) {
        super(itemView);

        postofofficial = (TextView) itemView.findViewById(R.id.postofofficial);
        nameofofficial = (TextView) itemView.findViewById(R.id.nameofofficial);


    }
}
