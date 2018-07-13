package gov.ap.tms.annadanam.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import gov.ap.tms.annadanam.R;

public class SplashActivity extends BaseActivity {

    public static final long HANDLER_DURATION = 3*1000;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, HANDLER_DURATION);
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(null);
    }
}
