package com.mustywzki.projettechl3image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                                        View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
