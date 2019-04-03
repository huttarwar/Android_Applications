package com.example.heramb.know_your_govt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {


    private static final String TAG = "OfficialAdapter";
    private ArrayList<Official_Person> officialPersonList;
    private MainActivity mainAct;

    public OfficialAdapter(ArrayList<Official_Person> stockList, MainActivity mainActivity) {
        this.officialPersonList = stockList;
        mainAct = mainActivity;
    }

    @Override
    public OfficialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);


        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);


        return new OfficialViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int position) {

        Official_Person officialPerson = officialPersonList.get(position);


        StringBuffer nameWithParty = new StringBuffer();
        nameWithParty.append(officialPerson.getNameofofficial());

        if(officialPerson.getParty() != null)
        {
            nameWithParty.append(" (");
            nameWithParty.append(officialPerson.getParty());
            nameWithParty.append(")  ");
        }
        holder.nameofofficial.setText(nameWithParty);
        holder.postofofficial.setText(officialPerson.getPostofofficial());

    }

    @Override
    public int getItemCount() {

        return officialPersonList.size();

    }
}
