package com.example.nasaspaceapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<ViewPagerItem> viewPagerItems;


    public ViewPagerAdapter(Context mContext, ArrayList<ViewPagerItem> items) {
        this.mContext = mContext;
        this.viewPagerItems = items;
    }

    @Override
    public int getCount() {
        return viewPagerItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item, container, false);
        ViewPagerItem item = viewPagerItems.get(position);
        ImageView imageView = itemView.findViewById(R.id.img_splash_background);
        imageView.setImageResource(item.getImageID());
        TextView tv_title = itemView.findViewById(R.id.tv_splash_header);
        tv_title.setText(item.getTitle());
        TextView tv_content = itemView.findViewById(R.id.tv_splash_description);
        tv_content.setText(item.getDescription());
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
