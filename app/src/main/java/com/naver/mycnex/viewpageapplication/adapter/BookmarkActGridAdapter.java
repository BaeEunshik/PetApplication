package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.custom.SquareImageView;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class BookmarkActGridAdapter extends BaseAdapter{
    /**
     * 현재 로그인 멤버의 북마크 장소 목록들을 가져와
     * BookmarkList_Activity 에 그리드뷰로 출력한다.
     */

    // TODO :
    // 북마크 데이터 구조 구현
    // 이미지 객체와 합친 arrList 로 만들어야함

    // 북마크 리스트
    ArrayList<Store> bookmarks;
    // 생성자

    @Override
    public int getCount() {
        return bookmarks.size();
    }
    @Override
    public Object getItem(int position) {
        return bookmarks.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmarks_grid, parent, false);
            // 1. 이미지
            // 2. 가게이름
            // 3. 평점
            // 4. 가게구분
            // 5. 구 ( ex. 강남구 ) , 현재 위치로부터의 거리 km
            // ( 6. 조회수 ??? )
            // ( 7. 리뷰수 ??? )
            holder.itemImg = convertView.findViewById(R.id.itemImg);
            holder.TextName = convertView.findViewById(R.id.textName);
            holder.textPlace = convertView.findViewById(R.id.textPlace);
            holder.TextDistance = convertView.findViewById(R.id.TextDistance);
            holder.TextPoint = convertView.findViewById(R.id.TextPoint);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        /** setText **/
        holder.TextName.setText(bookmarks.get(position).getName());    // 이름
        holder.TextDistance.setText("00km");                        // TODO : 거리 ( 구현? 삭제? )
        switch ( bookmarks.get(position).getCategory() ){                // 장소구분
            case 0: holder.textPlace.setText("일반카페");
                break;
            case 1: holder.textPlace.setText("일반식당");
                break;
            default: holder.textPlace.setText("invalid");
                break;
        }

        // TODO :
        // 평점 계산 로직)
        // DB 로부터 데이터를 받아올 때에
        // 해당 Store 에 달린 리뷰의 점수를 모두 더하여
        // 리뷰의 갯수만큼 나눈 다음에
        // - 객체 배열에 넣어서 사용
        holder.TextPoint.setText("5.0");    // 점수

        /** setIMG **/
        // 그리드 이미지
        GlideApp.with(parent.getContext())
                .load( R.drawable.dog1 )
                .centerCrop()
                .into( holder.itemImg );

        return convertView;
    }
    public class Holder{
        SquareImageView itemImg;
        TextView TextName;
        TextView TextDistance;
        TextView textPlace;
        TextView TextPoint;
    }
}
