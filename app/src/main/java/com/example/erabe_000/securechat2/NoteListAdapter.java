package com.example.erabe_000.securechat2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteListAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<Note> noteList;

    public NoteListAdapter(Context context, int layout, ArrayList<Note> noteList) {
        this.context = context;
        this.layout = layout;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView noteAbout, noteTime;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.noteAbout = (TextView) row.findViewById(R.id.notesAbout);
            holder.noteTime = (TextView) row.findViewById(R.id.notesTime);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Note note = noteList.get(position);

        holder.noteAbout.setText(note.getAbout());
        holder.noteTime.setText(note.getTime());

        return row;
    }
}
