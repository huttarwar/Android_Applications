package com.example.cs442huttarwar.multinotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<View_Holder> {

    private static final String TAG = "NoteAdapter";
    private List<Note> NoteList;
    private MainActivity mainAct;

    public NoteAdapter(List<Note> NoteList, MainActivity mainActivity) {
        this.NoteList = NoteList;
        mainAct = mainActivity;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view,viewGroup,false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder view_holder, int pos) {
        Note note= NoteList.get(pos);
        view_holder.title.setText(note.getTitle());
        view_holder.dateTime.setText(note.getDateTime());
        view_holder.description.setText(Substring(note.getDescription(),80));
    }

    private String Substring(String s, int length) {
        if(!TextUtils.isEmpty(s)){
            if(s.length() > length){
                return s.substring(0, length)+"...";
            }
        }
        return s;
    }

    @Override
    public int getItemCount() {
        return NoteList.size();
    }
}
