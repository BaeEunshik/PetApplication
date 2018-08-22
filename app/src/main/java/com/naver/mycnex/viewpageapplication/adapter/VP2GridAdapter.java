package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.naver.mycnex.viewpageapplication.R;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class VP2GridAdapter extends BaseAdapter{

    //테스트 위한 임시 리스트
    ArrayList<Integer> items;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        if( convertView == null ){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_vp1, parent, false);
            holder.photo = convertView.findViewById(R.id.itemImg);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

       // Integer item = (Integer)getItem(position);
       // holder.photo.setImageResource(item);

        return convertView;
    }
    public class Holder {
        ImageView photo;
    }
}
