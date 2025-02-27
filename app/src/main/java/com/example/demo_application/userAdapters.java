package com.example.demo_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class userAdapters extends ArrayAdapter<User> {
    ArrayList<User> list,orig;
    Context context;
    static int count = 0;


    public userAdapters(@NonNull Context context, ArrayList<User> list) {
        super(context, R.layout.item_card,list);
        this.context = context;
        this.list = list;
        this.count = list.size();
    }

    public static int getCountOfList(){
        return count;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<User> results = new ArrayList<User>();
                if (orig == null)
                    orig = list;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final User g : orig) {
                            if (g.getName().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    for(User i:results)
                        System.out.println(i.getId());
                    count = results.size();
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                list = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false);
        TextView t1,t2;
        t1 = v.findViewById(R.id.tvId);
        t2 = v.findViewById(R.id.tvName);

        if (position<list.size()) {
            User f = list.get(position);
            t1.setText(f.getId());
            t2.setText(f.getName());
        }
        return v;
    }
}

