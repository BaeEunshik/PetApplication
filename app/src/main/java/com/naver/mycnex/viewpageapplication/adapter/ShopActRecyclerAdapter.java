package com.naver.mycnex.viewpageapplication.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.naver.mycnex.viewpageapplication.PhotoActivity;
import com.naver.mycnex.viewpageapplication.PhotosActivity;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.TESTImage;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;

import java.util.ArrayList;

public class ShopActRecyclerAdapter extends RecyclerView.Adapter<ShopActRecyclerAdapter.Holder>{

    // 데이터 ( 테스트 )
    ArrayList<TESTImage> testImages;
    // Context
    private Context context;

    // 생성자 함수
    public ShopActRecyclerAdapter(ArrayList<TESTImage> testImages,Context context){
        this.testImages = testImages;
        this.context = context;
    }

    /******************** implement Method ********************/
    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Holder 객체를 리턴하여 onBindViewHolder 에서 사용한다.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_recycler, parent, false);
        Holder holder = new Holder(itemView);
        return holder;
    }
    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        // 리사이클러 뷰 생성
        if(position == testImages.size()){
            // 전체보기 버튼 생성
            holder.img.setVisibility(View.GONE);
        } else {
            // 버튼 삭제
            holder.btnMore.setVisibility(View.GONE);
            // 글라이드 이미지 적용
            TESTImage imageObj = testImages.get(position);
            GlideApp.with(context)
                    .load(imageObj.getImg())
                    .centerCrop()
                    .into(holder.img);
        }
        // 버튼 클릭 시 이동
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO :
                // 현재 가게의 모든 이미지 데이터 넘겨줘야 함
                context.startActivity(new Intent(context, PhotosActivity.class));
            }
        });
        // 사진 클릭 시 이동
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO :
                // 현재는 임시 객체의 getter() 사용 - 객체 변경
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("imgData",testImages.get(position).getImg());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        // 버튼 생성을 위해 item 을 한번 더 사용함
        return testImages.size() + 1;
    }


    /******************** HOLDER ********************/
    public class Holder extends RecyclerView.ViewHolder {
        private ImageView img;
        private Button btnMore;
        //생성자
        public Holder(View itemView) {
            super(itemView);
            if(itemView != null) {
                img = (ImageView) itemView.findViewById(R.id.img);
                btnMore = (Button) itemView.findViewById(R.id.btnMore);
            }
        }
    }

}
