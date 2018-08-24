package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Store;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class ShopListActListAdapter extends BaseAdapter {

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
            holder.txtName = convertView.findViewById(R.id.txtName);
            holder.txtAddress = convertView.findViewById(R.id.txtAddress);
            holder.txtPlace = convertView.findViewById(R.id.txtPlace);
            holder.txtIsCheck = convertView.findViewById(R.id.txtIsCheck);
            //////////
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        // 동작
        Store store = (Store)getItem(position);

        holder.txtName.setText(store.getName());
        holder.txtAddress.setText(store.getAddress());

        switch (store.getCategory()){                // 장소구분
            case 0: holder.txtPlace.setText("애견동반");
                break;
            case 1: holder.txtPlace.setText("애견전용");
                break;
            default: holder.txtPlace.setText("invalid");
                break;
        }
        // TODO :
        // 필드에 Store 검토여부 넣고
        /*if( 검토중){
            holder.txtIsCheck.setText("검토중");
        }*/
        holder.txtIsCheck.setText("검토중");   //임시

        return convertView;
    }

    public class Holder {
        TextView txtName;
        TextView txtAddress;
        TextView txtPlace;
        TextView txtIsCheck;
    }
}
