package com.example.rewardsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private Button btnShow;
    private RewardedAd rewardedAd;
    private static final String TAG = "TestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);




        rewardedAd  = new RewardedAd(this,"ca-app-pub-3940256099942544/5224354917");

        final RewardedAdLoadCallback rewardedAdLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {


            }

            @Override
            public void onRewardedAdFailedToLoad(int i) {
                super.onRewardedAdFailedToLoad(i);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), rewardedAdLoadCallback);

        //view init
        btnShow=findViewById(R.id.btn_show);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rewardedAd.isLoaded()){
                    RewardedAdCallback rewardedAdCallback = new RewardedAdCallback() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            Toast.makeText(TestActivity.this, "Reward: "+rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            RewardedAdLoadCallback rewardedAdLoadCallback2 = new RewardedAdLoadCallback() {
                                @Override
                                public void onRewardedAdLoaded() {


                                }

                                @Override
                                public void onRewardedAdFailedToLoad(int i) {
                                    super.onRewardedAdFailedToLoad(i);
                                }
                            };

                            rewardedAd.loadAd(new AdRequest.Builder().build(), rewardedAdLoadCallback2);
                            super.onRewardedAdClosed();
                        }
                    };
                    rewardedAd.show(TestActivity.this,rewardedAdCallback);
                }
                else{
                    Log.d(TAG, "Rewarded Ad wasn't loaded yet.");
                }
            }
        });

    }
}
