package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Member;
import com.naver.mycnex.viewpageapplication.data.Review;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class ReviewListAdapter extends BaseAdapter {
    ArrayList<Review> reviews;
    ArrayList<Member> members;

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       Holder holder = new Holder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_list, parent, false);
            holder.review_nickname_txt = convertView.findViewById(R.id.review_nickname_txt);
            holder.review_date_txt = convertView.findViewById(R.id.review_date_txt);
            holder.review_review_txt = convertView.findViewById(R.id.review_review_txt);

            convertView.setTag(holder);
        } else {
            holder = (ReviewListAdapter.Holder)convertView.getTag();
        }
      Review review = (Review)getItem(position);

        holder.review_nickname_txt.setText(members.get(position).getName());
        holder.review_date_txt.setText(review.getDay());
        holder.review_review_txt.setText(review.getContent());

        return convertView;
    }

    public class Holder {
        TextView review_nickname_txt;
        TextView review_date_txt;
        TextView review_review_txt;

    }
}



