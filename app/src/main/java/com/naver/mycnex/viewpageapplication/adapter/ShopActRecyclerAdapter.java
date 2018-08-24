package com.naver.mycnex.viewpageapplication.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.TESTImage;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;

import java.util.ArrayList;

public class ShopActRecyclerAdapter extends RecyclerView.Adapter<ShopActRecyclerAdapter.Holder>{

    // 데이터
    ArrayList<TESTImage> testImages;

    // Context
    private Context context;

    // 생성자
    public ShopActRecyclerAdapter(ArrayList<TESTImage> testImages,Context context){
        this.testImages = testImages;
        this.context = context;
    }

    /******************** implement Method ********************/
    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 홀더 객체를 리턴하는 함수
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_recycler, parent, false);

       // 여기서 item 레이아웃 속성들을 특정하여 HOLDER 객체로 생성하여 리턴함
        Holder holder = new Holder(itemView);
        return holder;
    }
    @Override
    public void onBindViewHolder(Holder holder, int position) {

        //객체 ( 임시 )
        TESTImage image = testImages.get(position);

        //비율 설정 ( 정사각형 )
        /*ImageView img = holder.img;
        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.width = layoutParams.height;
        Log.d("은식-레이아웃파람",Integer.toString(layoutParams.height));
        img.setLayoutParams(layoutParams);*/

        holder.img.setMinimumWidth(30);

        //글라이드 적용
        GlideApp.with(context)
        .load(image.getImg()) //키값으로 이미지 arrList 에서 가져온다?
        .centerCrop()
        .into(holder.img);
    }
    @Override
    public int getItemCount() {
        return testImages.size();
    }


    /** HOLDER **/
    public class Holder extends RecyclerView.ViewHolder {
        private ImageView img;
        //생성자
        public Holder(View itemView) {
            super(itemView);
            if(itemView != null) {
                img = (ImageView) itemView.findViewById(R.id.img);
            }
        }
    }

}
