package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Store;

import java.util.ArrayList;

public class ShopListListAdapter extends BaseAdapter {

    //테스트 스토어 리스트
    //TODO : 이미지 객체와 합친 arrList 로 만들어야함
    ArrayList<Store> stores;

    @Override
    public int getCount() {
        return stores.size();
    }
    @Override
    public Object getItem(int position) {
        return stores.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_list_list, parent, false);

            // 요소 정의


            //////////

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        // 동작

        return convertView;
    }

    public class Holder {
        TextView TextName;
        TextView TextDistance;
        TextView textPlace;
        TextView TextPoint;
    }
}
