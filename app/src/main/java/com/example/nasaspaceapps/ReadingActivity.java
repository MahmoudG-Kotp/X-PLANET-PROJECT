package com.example.nasaspaceapps;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

public class ReadingActivity extends AppCompatActivity {
    private WebView readingContentWV;
    private ImageButton darkModeIB, gotoTopIB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        initiateReadingItems();
        loadReadingContent(getClickedChapter());
    }

    private void initiateReadingItems() {
        readingContentWV = findViewById(R.id.wv_reading_content);
        readingContentWV.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if((v.getTop() - scrollY * v.getHeight()) == v.getTop())
                    hide_animation(gotoTopIB);
                else
                    show_animation(gotoTopIB);
            }
        });
        darkModeIB = findViewById(R.id.img_btn_dark_mode);
        gotoTopIB = findViewById(R.id.btn_go_to_top);
    }

    private Chapters getClickedChapter() {
        switch (getIntent().getIntExtra(getString(R.string.clicked_chapter_id_key), 0)){
            case R.id.cv_chapter_one:
                return Chapters.CHAPTER_ONE;
            case R.id.cv_chapter_two:
                return Chapters.CHAPTER_TWO;
            case R.id.cv_chapter_three:
                return Chapters.CHAPTER_THREE;
            case R.id.cv_chapter_four:
                return Chapters.CHAPTER_FOUR;
            case R.id.cv_chapter_five:
                return Chapters.CHAPTER_FIVE;
            case R.id.cv_chapter_six:
                return Chapters.CHAPTER_SIX;
            default:
                return Chapters.None;
        }
    }

    private void loadReadingContent(Chapters chapter) {
        if(isDarkModeEnabled()) {
            readingContentWV.loadUrl("file:///android_asset/" + chapter.toString().toLowerCase() + "_dark_mode.html");
            readingContentWV.setBackgroundColor(getColor(R.color.color_dark_black));
            darkModeIB.setBackgroundResource(R.drawable.disable_dark_mode_bttn_background);
            darkModeIB.setImageResource(R.drawable.ic_disable_dark_mode);
            gotoTopIB.setBackgroundResource(R.drawable.disable_dark_mode_bttn_background);
            gotoTopIB.setImageResource(R.drawable.ic_arrow_go_to_top_dark_mode);
        }
        else {
            readingContentWV.loadUrl("file:///android_asset/" + chapter.toString().toLowerCase() + "_light_mode.html");
            readingContentWV.setBackgroundColor(getColor(R.color.color_white));
            darkModeIB.setBackgroundResource(R.drawable.enable_dark_mode_bttn_background);
            darkModeIB.setImageResource(R.drawable.ic_enable_dark_mode);
            gotoTopIB.setBackgroundResource(R.drawable.enable_dark_mode_bttn_background);
            gotoTopIB.setImageResource(R.drawable.ic_arrow_go_to_top_light_mode);
        }
        WebSettings webSettings = readingContentWV.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setChapterScrollPosition(getClickedChapter(), readingContentWV);
            }
        },200);
    }

    public void toggleDarkModeStatus(View view){
        saveChapterScrollPosition(getClickedChapter(), readingContentWV);
        Animation fadeDarkModeSrcIn = AnimationUtils.loadAnimation(this, R.anim.fade_dark_mode_src_in);
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.dark_mode_status_key), MODE_PRIVATE).edit();
        editor.putBoolean(getString(R.string.is_dark_mode_enabled_key), !isDarkModeEnabled());
        editor.apply();
        gotoTopIB.startAnimation(fadeDarkModeSrcIn);
        view.startAnimation(fadeDarkModeSrcIn);
        loadReadingContent(getClickedChapter());
    }

    public void goToTop(View view){
        ObjectAnimator anim = ObjectAnimator.ofInt(readingContentWV,
                "scrollY", readingContentWV.getScrollY(), 0);
        anim.setDuration(500);
        anim.start();
        saveChapterScrollPosition(getClickedChapter(), readingContentWV);

    }

    public void show_animation(final View view) {
        Animation slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_go_to_top_button_down);
        if(!view.isShown())
            view.startAnimation(slideUpAnim);

        slideUpAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
    }

    public void hide_animation(final View view){
        Animation slideDownAnim = AnimationUtils.loadAnimation(this, R.anim.slide_go_to_top_button_up);
        if (view.isShown())
            view.startAnimation(slideDownAnim);
        slideDownAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                view.clearAnimation();
            }
        });
    }

    private boolean isDarkModeEnabled(){
        return getSharedPreferences(getString(R.string.dark_mode_status_key), MODE_PRIVATE).getBoolean(getString(R.string.is_dark_mode_enabled_key), false);
    }

    private void saveChapterScrollPosition(Chapters chapter, WebView webView){
        SharedPreferences.Editor editor = getSharedPreferences(chapter.name()+getString(R.string.chapter_scroll_position_key), MODE_PRIVATE).edit();
        editor.putInt(getString(R.string.chapter_scroll_position_key), webView.getScrollY());
        editor.apply();
    }

    private int getChapterScrollPosition(Chapters chapter){
        return getSharedPreferences(chapter.name()+getString(R.string.chapter_scroll_position_key), MODE_PRIVATE).getInt(getString(R.string.chapter_scroll_position_key), readingContentWV.getTop());
    }

    private void setChapterScrollPosition(Chapters chapter, WebView webView){
        int chapterScrollPosition = getChapterScrollPosition(chapter);
        if (webView.getTop() != chapterScrollPosition){
            ObjectAnimator anim = ObjectAnimator.ofInt(webView, "scrollY",
                    webView.getScrollY(), chapterScrollPosition);
            anim.setDuration(500);
            anim.start();
        }
    }

    @Override
    protected void onPause() {
        saveChapterScrollPosition(getClickedChapter(), this.readingContentWV);
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveChapterScrollPosition(getClickedChapter(), this.readingContentWV);
        super.onStop();
    }

    enum Chapters {
        None,
        CHAPTER_ONE,
        CHAPTER_TWO,
        CHAPTER_THREE,
        CHAPTER_FOUR,
        CHAPTER_FIVE,
        CHAPTER_SIX
    }

}
