package com.example.rewardsample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    private TextView pointsTextView;
    private Button btnView;
    private int points;
    private  SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    
    final static  String TAG=">>>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //app id
        //ca-app-pub-9607577251750757~6876615291
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: completed");
            }
        });

        //init
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        //sharedPref init
        sharedPreferences=getApplicationContext().getSharedPreferences("pointsPref",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        points=sharedPreferences.getInt("points",0);
        


        //view init
        pointsTextView=findViewById(R.id.points_textview);
            pointsTextView.setText(Integer.toString(points));

        
        btnView=findViewById(R.id.btn_view);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRewardedVideoAd.isLoaded()){
                    mRewardedVideoAd.show();
                }

            }
        });



    }

    //ca-app-pub-3940256099942544/5224354917
    private void loadRewardedVideoAd() {
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
            new AdRequest.Builder().build());
        }else{
            Toast.makeText(this, "already loaded", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        //storing rewarded points in cache
        editor.putInt("points",points+rewardItem.getAmount());
        editor.commit();

        pointsTextView.setText(Integer.toString(sharedPreferences.getInt("points",0)));

        showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Congratulations!!");
        alertDialog.setMessage("You are rewarded with 10 points.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    protected void onResume() {
        Toast.makeText(this, "Resumed!!", Toast.LENGTH_SHORT).show();
        mRewardedVideoAd.resume(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "Paused!!", Toast.LENGTH_SHORT).show();
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
