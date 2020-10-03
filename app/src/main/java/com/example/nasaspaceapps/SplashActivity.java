package com.example.nasaspaceapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private LinearLayout viewpagerIndicator;
    private int dotsCount;
    private ImageView[] dots;
    private Button skipSplashBttn;
    private ViewPager splashViewPager;
    private int previous_pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initiateSplashItems();
        setSplashSkipped(false);
        setUpViewPager(splashViewPager, AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_splash_background_img_in));
    }

    private void initiateSplashItems() {
        skipSplashBttn = findViewById(R.id.btn_splash_skip);
        viewpagerIndicator = findViewById(R.id.viewPagerCountDots);
        splashViewPager = findViewById(R.id.viewpager_board);
    }

    private void setUpViewPager(ViewPager viewPager, Animation animation) {
        ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
        loadViewPagerData(viewPagerItems);
        ViewPagerAdapter splashViewPagerAdapter = new ViewPagerAdapter(this, viewPagerItems);
        viewPager.setAdapter(splashViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.startAnimation(animation);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(SplashActivity.this, R.drawable.non_selected_item_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(SplashActivity.this, R.drawable.selected_item_dot));
                int pos=position+1;
                if(pos == dotsCount&&previous_pos==(dotsCount-1))
                    show_animation();
                else if(pos == (dotsCount-1)&&previous_pos==dotsCount)
                    hide_animation();
                previous_pos = pos;
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        setUpUiViewPagerController(splashViewPagerAdapter);
    }

    private void setUpUiViewPagerController(ViewPagerAdapter viewPagerAdapter) {
        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];
        for (int dot = 0; dot < dotsCount; dot++) {
            dots[dot] = new ImageView(this);
            dots[dot].setImageDrawable(ContextCompat.getDrawable(SplashActivity.this, R.drawable.non_selected_item_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(6, 0, 6, 0);
            viewpagerIndicator.addView(dots[dot], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(SplashActivity.this, R.drawable.selected_item_dot));
    }

    public void loadViewPagerData(ArrayList<ViewPagerItem> viewPagerItems){
        String[] header = getResources().getStringArray(R.array.splash_headers);
        String[] description = getResources().getStringArray(R.array.splash_descriptions);
        int[] imageId = {R.drawable.splash_background_1, R.drawable.splash_background_3, R.drawable.splash_background_2, R.drawable.splash_background_4};
        for(int cellCounter = 0 ; cellCounter < imageId.length ; cellCounter++)
        {
            ViewPagerItem item = new ViewPagerItem();
            item.setImageID(imageId[cellCounter]);
            item.setTitle(header[cellCounter]);
            item.setDescription(description[cellCounter]);
            viewPagerItems.add(item);
        }
    }

    @Override
    public void onBackPressed(){

    }

    public void show_animation() {
        Animation slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_skip_splash_button_up);
        skipSplashBttn.startAnimation(slideUpAnim);

        slideUpAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                skipSplashBttn.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                skipSplashBttn.clearAnimation();
            }
        });
    }

    public void hide_animation(){
        Animation slideDownAnim = AnimationUtils.loadAnimation(this, R.anim.slide_skip_splash_button_down);
        skipSplashBttn.startAnimation(slideDownAnim);

        slideDownAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                skipSplashBttn.clearAnimation();
                skipSplashBttn.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void splashSkip(View view) {
        setSplashSkipped(true);
        finish();
    }

    public void setSplashSkipped(Boolean flag) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.splash_status_key), MODE_PRIVATE).edit();
        editor.putBoolean(getString(R.string.is_splash_skipped_key), flag);
        editor.apply();
    }
}