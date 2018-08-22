package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Store;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class VP1GridAdapter extends BaseAdapter{

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
        if( convertView == null ){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_vp1, parent, false);

            holder.TextName = convertView.findViewById(R.id.TextName);
            holder.textPlace = convertView.findViewById(R.id.TextPoint);
            holder.TextDistance = convertView.findViewById(R.id.TextDistance);
            holder.TextPoint = convertView.findViewById(R.id.TextPoint);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        /*
        private long id;
        private String name;
        private Integer contact;
        private Integer dog_size;
        private String store_information;
        private String operation_day;
        private String operation_time;
        private Integer parking;
        private Integer reservation;
        private String address;
        private String sigungu;
        private String dong;
        private double latitude;
        private double longitude;
        private Integer category;
        */
        holder.TextName.setText(stores.get(position).getName());    // 이름
        holder.TextDistance.setText("00km");                        // TODO : 거리 ( 구현? 삭제? )
        switch (stores.get(position).getCategory()){                // 장소구분
            case 0: holder.textPlace.setText("애견동반");
                    break;
            case 1: holder.textPlace.setText("애견전용");
                    break;
            default: holder.textPlace.setText("invalid");
                    break;
        }
        // TODO :
        // 로직)
        // DB 로부터 데이터를 받아올 때에
        // 해당 Store 에 달린 리뷰의 점수를 모두 더하여
        // 갯수만큼 나눈 다음에
        // - 객체 배열에 넣어서 사용
        holder.TextPoint.setText("5.0");    // 점수


        return convertView;
    }
    public class Holder {
        TextView TextName;
        TextView TextDistance;
        TextView textPlace;
        TextView TextPoint;
    }
}
