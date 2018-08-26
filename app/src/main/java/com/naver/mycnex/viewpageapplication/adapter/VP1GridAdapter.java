package com.naver.mycnex.viewpageapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.ShopActivity;
import com.naver.mycnex.viewpageapplication.custom.SquareImageView;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;

import java.util.ArrayList;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VP1GridAdapter extends BaseAdapter{

    //테스트 스토어 리스트
    //TODO : 이미지 객체와 합친 arrList 로 만들어야함
    ArrayList<Store> stores;

    public VP1GridAdapter(ArrayList<Store> stores){
        this.stores = stores;
    }

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
    public View getView(int position, View convertView, final ViewGroup parent) {

        Holder holder = new Holder();
        if( convertView == null ){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vp1_grid, parent, false);

            holder.itemImg = convertView.findViewById(R.id.itemImg);
            holder.contents = convertView.findViewById(R.id.contents);
            holder.TextName = convertView.findViewById(R.id.TextName);
            holder.textPlace = convertView.findViewById(R.id.TextPoint);
            holder.TextDistance = convertView.findViewById(R.id.TextDistance);
            holder.TextPoint = convertView.findViewById(R.id.TextPoint);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        // setText
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
        // setIMG
        GlideApp.with(parent.getContext())
                .load( R.drawable.dog1 )
                .centerCrop()
                .into( holder.itemImg );

        // TODO :
        // 평점 계산 로직)
        // DB 로부터 데이터를 받아올 때에
        // 해당 Store 에 달린 리뷰의 점수를 모두 더하여
        // 갯수만큼 나눈 다음에
        // - 객체 배열에 넣어서 사용
        holder.TextPoint.setText("5.0");    // 점수


        // 이미지 클릭시 이동
        holder.itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = parent.getContext();
                Intent intent = new Intent(context,ShopActivity.class);
                context.startActivity(intent);
            }
        });
        // 내용 클릭시 이동
        holder.contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = parent.getContext();
                Intent intent = new Intent(context,ShopActivity.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public class Holder {
        SquareImageView itemImg;
        LinearLayout contents;
        TextView TextName;
        TextView TextDistance;
        TextView textPlace;
        TextView TextPoint;
    }
}
