package com.example.nasaspaceapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

 public class ChaptersActivity extends AppCompatActivity {
    private static Boolean isChaptersViewVisible = false;
    private Boolean isBackgroundAnimationEnded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        if (!isSplashSkipped())
            startSplashActivity();
        gridLayoutListener();
    }

     private void gridLayoutListener() {
         GridLayout chaptersGridLayout = findViewById(R.id.gl_chapters_activity);
         final Animation scaleClickAnim = AnimationUtils.loadAnimation(this, R.anim.scale_chapters_card_click);
         for (int cardCount = 0; cardCount < chaptersGridLayout.getChildCount(); cardCount++) {
            CardView chapterCard = (CardView) chaptersGridLayout.getChildAt(cardCount);
            chapterCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (isBackgroundAnimationEnded()) {
                        view.setEnabled(false);
                        view.startAnimation(scaleClickAnim);
                        scaleClickAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                startActivity(new Intent(ChaptersActivity.this, ReadingActivity.class).putExtra(getString(R.string.clicked_chapter_id_key), view.getId()));
                                overridePendingTransition(R.anim.slide_reading_activity_up, R.anim.fade_chapters_activity_in);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        view.setEnabled(true);
                    }
                }
            });
         }
     }

     @Override
     protected void onResume() {
        if(isSplashSkipped() && !isChaptersViewVisible)
        {
            isChaptersViewVisible = true;
            findViewById(R.id.chapters_activity_view).setVisibility(View.VISIBLE);
            slideDownAnimation(findViewById(R.id.img_chapters_background));
        }
        super.onResume();
     }

     private void slideDownAnimation(View view){
         Animation slideDownAnim = AnimationUtils.loadAnimation(this, R.anim.slide_chapters_activity_background_down);
         view.startAnimation(slideDownAnim);
         slideDownAnim.setAnimationListener(new Animation.AnimationListener() {
             @Override
             public void onAnimationStart(Animation animation) {

             }

             @Override
             public void onAnimationEnd(Animation animation) {
                setBackgroundAnimationEnded(true);
             }

             @Override
             public void onAnimationRepeat(Animation animation) {

             }
         });
     }

     private void startSplashActivity() {
         findViewById(R.id.chapters_activity_view).setVisibility(View.INVISIBLE);
         startActivity(new Intent(ChaptersActivity.this, SplashActivity.class));
     }

     public Boolean isSplashSkipped() {
         return getSharedPreferences(getString(R.string.splash_status_key), MODE_PRIVATE).getBoolean(getString(R.string.is_splash_skipped_key), false);
     }

     public Boolean isBackgroundAnimationEnded() {
         return isBackgroundAnimationEnded;
     }

     public void setBackgroundAnimationEnded(Boolean animationEnded) {
         isBackgroundAnimationEnded = animationEnded;
     }
 }