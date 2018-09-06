package com.naver.mycnex.viewpageapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.RegisterShopActivity;
import com.naver.mycnex.viewpageapplication.ShopActivity;
import com.naver.mycnex.viewpageapplication.custom.SquareImageView;
import com.naver.mycnex.viewpageapplication.data.ImageFile;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;
import com.naver.mycnex.viewpageapplication.global.Global;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class VP1GridAdapter extends BaseAdapter{

    // 테스트 스토어 리스트
    // TODO :
    // 이미지 객체와 합친 arrList 로 만들어야함
    ArrayList<Store> stores;
    ArrayList<ImageFile> images;

    @Override
    public int getCount() {
        return stores.size();
    }
    @Override
    public Store getItem(int position) {
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
            // 0. 이미지
            // 1. 북마크
            // 2. 가게이름
            // 3. 평점
            // 4. 가게구분
            // 5. 구 ( ex. 강남구 ) , 현재 위치로부터의 거리 km
            // ( 6. 조회수 ??? )
            // ( 7. 리뷰수 ??? )
            holder.itemImg = convertView.findViewById(R.id.itemImg);
            holder.btnBookmark = convertView.findViewById(R.id.btnBookmark);
            holder.textName = convertView.findViewById(R.id.textName);
            holder.textPlace = convertView.findViewById(R.id.textPlace);
            holder.textDistance = convertView.findViewById(R.id.TextDistance);
            holder.textPoint = convertView.findViewById(R.id.TextPoint);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        // 그리드뷰 아이템 세팅
        Store store = getItem(position);
        // Context
        Context context = parent.getContext();

        /** setText **/

        //장소이름
        holder.textName.setText(store.getName());

        //현재위치로부터의 거리
        holder.textDistance.setText("00km"); // TODO : ( 구현? 삭제? )

        //카테고리 ( 장소구분 )
        // DB 필드값 : Global 클래스의 CATEGORY_GENERAL_ARR 배열에서 인덱스 값으로 사용할 수 있도록 설계
        if( store.getCategory() >= Global.CATEGORY_DIVISION_NUM) {
            holder.textPlace.setText(Global.CATEGORY_SPECIAL_STR_ARR[ store.getCategory()-Global.CATEGORY_SPECIAL_CAFE ]);
        } else {
            holder.textPlace.setText(Global.CATEGORY_GENERAL_STR_ARR[ store.getCategory() ]);
        }

        // TODO :
        // 평점 계산 로직)
        // 리뷰가 작성될 때마다
        // 리뷰카운트 & 점수(합계) 를 저장해서
        // 불러올 때 리뷰점수 / 리뷰카운트 로 점수를 계산
         /*DB 로부터 데이터를 받아올 때에
         해당 Store 에 달린 리뷰의 점수를 모두 더하여
         리뷰의 갯수만큼 나눈 다음에
         - 객체 배열에 넣어서 사용*/
        holder.textPoint.setText("5.0");    // 점수

        /** setIMG **/

        // 그리드 이미지
        GlideApp.with(context)
                .load(Global.BASE_IMAGE_URL+images.get(position).getSavedName())
                .centerCrop()
                .into( holder.itemImg );

        // 북마크 여부 표시 ( TODO : 북마크 여부에 따라 다른 이미지 적용 )
        GlideApp.with(context)
                .load( R.drawable.star_on )
                .fitCenter()
                .into( holder.btnBookmark );

        /** OnClick **/
        final Holder onClickHolder = holder;
        holder.btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 북마크에 저장, 북마크 여부에 따른  이미지 교체
                GlideApp.with(parent.getContext())
                        .load( R.drawable.star_off )
                        .fitCenter()
                        .into( onClickHolder.btnBookmark );
            }
        });
        return convertView;
    }



    public class Holder {
        SquareImageView itemImg;
        ImageView btnBookmark;
        TextView textName;
        TextView textDistance;
        TextView textPlace;
        TextView textPoint;
    }
}
