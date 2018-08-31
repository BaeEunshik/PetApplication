package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Comment;

import java.util.ArrayList;

public class CommentListAdapter extends BaseAdapter {
    ArrayList<Comment> items;

    public CommentListAdapter(ArrayList<Comment> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
            holder.txt_comment = convertView.findViewById(R.id.txt_comment);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        Comment item = (Comment)getItem(position);

        holder.txt_comment.setText(item.getComment());

        return convertView;
    }

    public class Holder {
        TextView txt_comment;
    }
}