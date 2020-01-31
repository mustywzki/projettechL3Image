package com.mustywzki.projettechl3image;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mustywzki.projettechl3image.Algorithms.Functions;

public class MainActivity extends AppCompatActivity {

    public enum AlgorithmType {
        GRAY,
        COLORIZE,
        COLOR_RANGE,
        DYNAMIC_EXTENSION,
        HIST_EQUALIZER,
        AVERAGE_FILTER,
        GAUSSIAN_FILTER,
        PREWITT_FILTER,
        SOBEL_FILTER,
        LAPLACIEN_FILTER,
        NEGATIVE,
        HUE,
        BRIGHTNESS
    }

    private AlgorithmType currentAlgorithm;

    // GUI-related members
    private ImageView imageView;
    private Bitmap currentBmp;
    private Bitmap processedBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE);

        imageView = findViewById(R.id.picture);

        currentBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public void onClickAlgorithms(View v) {
        switch (v.getId()) {
            case R.id.gray_button:
                currentAlgorithm = AlgorithmType.GRAY;
                //seekbars_load(true,"Red",100,true,"Green",100,true,"Blue",100);
                // Default bars
                /* bar1.setProgress(30);
                bar2.setProgress(11);
                bar3.setProgress(59); */
                break;
            case R.id.random_button:
                currentAlgorithm = AlgorithmType.COLORIZE;
                //seekbars_load(true,"Hue",359,false,"",1,false, "",1);
                break;
            case R.id.selected_color_button:
                currentAlgorithm = AlgorithmType.COLOR_RANGE;
                //seekbars_load(true,"Hue",359,true,"Chroma Key",180,false,"",1);
                break;
            case R.id.filter_button:
                //currentAlgorithm = AlgorithmType.CONVOLUTION;
                //seekbars_load(true,"Range",128,false,"",1,false,"",1);
                //bar1.setProgress(64);
                break;
            case R.id.linear_transformation_button:
                currentAlgorithm = AlgorithmType.DYNAMIC_EXTENSION;
                //seekbars_load(false,"",1,false,"",1,false,"",1);
                break;
            case R.id.egalisation_histogram_button:
                currentAlgorithm = AlgorithmType.HIST_EQUALIZER;
                //seekbars_load(false,"",1,false,"",1,false,"",1);
                break;
            case R.id.negative_button:
                currentAlgorithm = AlgorithmType.NEGATIVE;
                break;
        }
        applyProcessings();
    }

    public void applyProcessings(){

        switch (currentAlgorithm){
            case GRAY:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                //Functions.toGray(processedBmp,bar1.getProgress()/100.0,bar2.getProgress()/100.0,bar3.getProgress()/100.0);
                break;
            case COLORIZE:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                //Functions.colorize(processedBmp,bar1.getProgress());
                break;
            case COLOR_RANGE:
                processedBmp = currentBmp.copy(currentBmp.getConfig(),true);
                //Functions.keepColor(processedBmp,bar1.getProgress(),bar2.getProgress());
                break;
            case DYNAMIC_EXTENSION:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                //Functions.computeDynamicLinearExtension(Utils.getHistogram(processedBmp),processedBmp);
                break;
            case HIST_EQUALIZER:
                processedBmp = currentBmp.copy(currentBmp.getConfig(),true);
                //Functions.histogramEqualizer(Utils.getHistogram(processedBmp),processedBmp);
                break;
            case NEGATIVE:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                Functions.negative(processedBmp);

        }
        imageView.setImageBitmap(processedBmp);
        currentBmp = processedBmp;
    }
}
