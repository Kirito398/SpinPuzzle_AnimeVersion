package com.bg.spinpuzzleanime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	private InterstitialAd interstitial;
	@SuppressLint("MissingPermission")
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        MobileAds.initialize(this, "ca-app-pub-9648937932604603~1619299392");
		initialize(new SpinPuzzleAnime(this), config);
	}

	public void loadAd(){
	    handler.sendEmptyMessage(0);
    }

    public void loadInterstitial(){
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-9648937932604603/3918159673");

        // Создание запроса объявления.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Запуск загрузки межстраничного объявления.
        interstitial.loadAd(adRequest);
    }

    public void showInterstitial(){
        handler.sendEmptyMessage(1);
    }

    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==1){
                interstitial.show();
            }else{
                loadInterstitial();
            }
        }
    };
}
