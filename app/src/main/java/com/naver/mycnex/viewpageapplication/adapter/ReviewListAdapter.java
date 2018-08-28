package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Review;

import java.util.ArrayList;

public class ReviewListAdapter extends BaseAdapter {
    ArrayList<Review> items;


    public ReviewListAdapter(ArrayList<Review> items) {
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_review_write, parent, false);
            holder.txt_review = convertView.findViewById(R.id.edit_write);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Review item = (Review) getItem(position);

        holder.txt_review.setText(item.getReview());

        return convertView;
    }

    public class Holder {
        TextView txt_review;
    }
}