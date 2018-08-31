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
    ArrayList<Review> items ;

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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       ReviewListAdapter.Holder holder = new ReviewListAdapter.Holder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_list, parent, false);
            holder.txt_review_content = convertView.findViewById(R.id.txt_review_content);

            convertView.setTag(holder);
        } else {
            holder = (ReviewListAdapter.Holder)convertView.getTag();
        }
      Review item = (Review)getItem(position);

        holder.txt_review_content.setText(item.getContent());

        return convertView;
    }

    public class Holder {
        TextView txt_review_content;
    }
}



