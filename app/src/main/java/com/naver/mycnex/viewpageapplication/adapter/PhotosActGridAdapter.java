package com.naver.mycnex.viewpageapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.naver.mycnex.viewpageapplication.PhotoActivity;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.TESTImage;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;

import java.util.ArrayList;

public class PhotosActGridAdapter extends BaseAdapter {

    // 데이터 ( 테스트 )
    ArrayList<TESTImage> testImages;
    // Context
    Context context;
    // 생성자
    public PhotosActGridAdapter(ArrayList<TESTImage> testImages){
        this.context = context;
        this.testImages = testImages;
    }

    @Override
    public int getCount() {
        return testImages.size();
    }
    @Override
    public Object getItem(int position) {
        return testImages.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Holder holder = new Holder();

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photos_grid,parent,false);

            holder.img = convertView.findViewById(R.id.img);

            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }

        // 글라이드
        TESTImage imageObj = testImages.get(position);
        GlideApp.with(parent.getContext())
                .load(imageObj.getImg())
                .centerCrop()
                .into(holder.img);

        // 클릭 시
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO :
                // 현재 사용하는 데이터 객체는 임시용 - 바꿔야함
                Context context = parent.getContext();  // Context
                TESTImage imageObj = testImages.get(position);  // 이미지 데이터 객체 생성
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("imgData",imageObj.getImg());   // 이미지 전달
                context.startActivity(intent);  //실행
            }
        });

        return convertView;
    }
    public class Holder{
        ImageView img;
    }
}
