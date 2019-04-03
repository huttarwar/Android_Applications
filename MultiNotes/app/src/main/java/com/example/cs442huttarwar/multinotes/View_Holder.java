package com.example.cs442huttarwar.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class View_Holder extends RecyclerView.ViewHolder {
    TextView title;
    TextView description;
    TextView dateTime;

    public View_Holder(View view){
        super(view);
        title = view.findViewById(R.id.Title);
        description = view.findViewById(R.id.view_description);
        dateTime = view.findViewById(R.id.date_time);
    }
}
