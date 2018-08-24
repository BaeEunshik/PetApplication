package com.naver.mycnex.viewpageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Bookmark;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookmarkActGridAdapter extends BaseAdapter{
    /**
     * 현재 로그인 멤버의 북마크 장소 목록들을 가져와
     * BookmarkList_Activity 에 그리드뷰로 출력한다.
     */

    // TODO : item 생성및 적용, 객체 생성및 적용 등...

    // 북마크 리스트
    ArrayList<Bookmark> bookmarks;
    // 생성자
    public BookmarkActGridAdapter(ArrayList<Bookmark> bookmarks){
        this.bookmarks = bookmarks;
    }

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

            //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.그리드아이템, parent, false);

            //북마크의 그리드뷰 내용

            // 1. 이미지
            // 2. 가게이름
            // 3. 평점
            // 4. 카페구분
            // 5. 구 ( ex. 강남구 ) , 현재 위치로부터의 거리 km
            // 6. 조회수
            // 7. 리뷰수

            /**
                holder.shopImg = convertView.findViewById(R.id.);
                holder.shopName = convertView.findViewById(R.id.);
                holder.point = convertView.findViewById(R.id.);
                holder.placeKind = convertView.findViewById(R.id.);
                holder.location_gu = convertView.findViewById(R.id.);
                holder.location_km = convertView.findViewById(R.id.);
                holder.viewNum = convertView.findViewById(R.id.);
                holder.reviewNum = convertView.findViewById(R.id.);
            */

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

            /**
             // 이미지
                GlideApp.with(context)
                 .load( 이미지 경로 - 객체로부터 가져와야 할)
                 .into(holder.shopImg);

             // 텍스트
                holder.shopName.setText( 장소이름 );
                holder.point.setText( 평점 );
                holder.placeKind.setText( 장소구분 );
                holder.location_gu.setText( 구( ex.강남구 ) );
                holder.location_km.setText( 현재위치로부터의 거리 km );
                holder.viewNum.setText( 조회수 );
                holder.reviewNum.setText( 리뷰갯수 );
            */

        return convertView;
    }
    public class Holder{
        ImageView shopImg;
        TextView shopName;
        TextView point;
        TextView placeKind;
        TextView location_gu;
        TextView location_km;
        TextView viewNum;
        TextView reviewNum;
    }
}
