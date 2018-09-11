package com.naver.mycnex.viewpageapplication.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.custom.SquareImageView;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreData;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.gps.GpsInfo;

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
    ArrayList<StoreData> storeData;
    // 생성자

    @Override
    public int getCount() {
        return storeData.size();
    }
    @Override
    public Object getItem(int position) {
        return storeData.get(position);
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
            holder.itemImg = convertView.findViewById(R.id.itemImg);
            holder.TextName = convertView.findViewById(R.id.textName);
            holder.textPlace = convertView.findViewById(R.id.textPlace);
            holder.TextDistance = convertView.findViewById(R.id.TextDistance);
            holder.TextPoint = convertView.findViewById(R.id.TextPoint);
            holder.review_count_txt = convertView.findViewById(R.id.review_count_txt);
            holder.View_txt = convertView.findViewById(R.id.View_txt);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        Store store = storeData.get(position).getStore();
        Context context = parent.getContext();

        /** setText **/
        holder.TextName.setText(store.getName());    // 이름
        holder.TextDistance.setText(getDistance(context,position) + "m");

        //리뷰 수
        holder.review_count_txt.setText(String.valueOf(storeData.get(position).getReviews().size()));

        //카테고리 ( 장소구분 )
        // DB 필드값 : Global 클래스의 CATEGORY_GENERAL_ARR 배열에서 인덱스 값으로 사용할 수 있도록 설계
        if( store.getCategory() >= Global.CATEGORY_DIVISION_NUM) {
            holder.textPlace.setText(Global.CATEGORY_SPECIAL_STR_ARR[ store.getCategory()-Global.CATEGORY_SPECIAL_CAFE ]);
        } else {
            holder.textPlace.setText(Global.CATEGORY_GENERAL_STR_ARR[ store.getCategory() ]);
        }

        double result = ((double)store.getScore_sum())/((double)store.getScore_count());
        double getPrimeNum = Math.ceil(result*10d) / 10d;

        if (Double.isNaN(getPrimeNum)) {
            holder.TextPoint.setText("0.0");
        } else {
            holder.TextPoint.setText(String.valueOf(getPrimeNum));
        }
        holder.View_txt.setText(store.getHit().toString());

        /** setIMG **/
        // 그리드 이미지
        GlideApp.with(context)
                .load(Global.BASE_IMAGE_URL+storeData.get(position).getImages().get(0).getSavedName())
                .centerCrop()
                .into( holder.itemImg );

        return convertView;
    }
    public int getDistance(Context context, int position) {

        LatLng latLng = getCurrentLocationByGPS(context);

        Location location1 = new Location("location1");
        location1.setLatitude(latLng.latitude);
        location1.setLongitude(latLng.longitude);

        Location location2 = new Location("location2");
        location2.setLatitude(storeData.get(position).getStore().getLatitude());
        location2.setLongitude(storeData.get(position).getStore().getLongitude());

        int distance = (int)location1.distanceTo(location2);

        return distance;
    }

    public LatLng getCurrentLocationByGPS(Context context) {
        GpsInfo gps = new GpsInfo(context);

        double lat = 0;
        double lng = 0;

        if (gps.isGetLocation()) {

            lat = gps.getLatitude();
            lng = gps.getLongitude();

        }

        LatLng currentLocation = new LatLng(lat, lng);
        return currentLocation;
    }

    public class Holder{
        SquareImageView itemImg;
        TextView TextName;
        TextView TextDistance;
        TextView textPlace;
        TextView TextPoint;
        TextView review_count_txt;
        TextView View_txt;
    }
}
