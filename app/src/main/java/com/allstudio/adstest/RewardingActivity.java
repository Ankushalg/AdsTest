package com.allstudio.adstest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardingActivity extends AppCompatActivity {
    private RewardedAd mRewardedAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarding);
        findViewById(R.id.ibr_load).setOnClickListener(view -> loadAd());
        findViewById(R.id.ibr_show).setOnClickListener(view -> showAd());
        loadAd();
    }

    private void showAd() {
        if (mRewardedAd != null) {
            mRewardedAd.show(RewardingActivity.this, rewardItem -> {
                // Handle the reward.
                Log.d("TAG", "The user earned the reward.");
                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();
                ts("The user earned reward.\nReward Amount: " + rewardAmount + "\nReward Type: " + rewardType);
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            ts("Ad Not Loaded Yet");
        }
    }

    private void loadAd() {
        if(mRewardedAd == null){
            ts("Loading Ad... Please wait...");
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(this, getResources().getString(R.string.adr_unit_id),
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d("TAG", loadAdError.getMessage());
                            mRewardedAd = null;
                            ts("Ad Loading Failed");
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            Log.d("TAG", "Ad was loaded.");
                            ts("Ad Loaded");
                            setCallBacks();
                        }
                    });
        } else {
            ts("Ad Loaded");
        }
    }

    private void setCallBacks() {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
                ts("Ad Dismissed");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
                ts("Failed To Show Ad.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                mRewardedAd = null;
                Log.d("TAG", "The ad was shown.");
                ts("Ad Shown to User");
            }
        });
    }

    private void ts(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
