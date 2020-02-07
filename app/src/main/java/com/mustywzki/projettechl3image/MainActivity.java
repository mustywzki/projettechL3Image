package com.mustywzki.projettechl3image;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mustywzki.projettechl3image.Algorithms.Contrast;
import com.mustywzki.projettechl3image.Algorithms.Functions;
import com.mustywzki.projettechl3image.Algorithms.Tools;

public class MainActivity extends AppCompatActivity {

    // Seekbar tab
    private View slider_bars;
    private FrameLayout buttons_view;
    private HorizontalScrollView button_scroll;
    private SeekBar bar1, bar2, bar3;
    private boolean isSliding;

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
    private Bitmap savedBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE);

        imageView = findViewById(R.id.picture);
        slider_bars = View.inflate(this,R.layout.seekbar_view,null);
        button_scroll = findViewById(R.id.button_scroll);
        buttons_view = findViewById(R.id.button_view);

        currentBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        savedBmp = currentBmp;

        setSeekBar();

    }

    public void setSeekBar(){
        // Option sliders listeners
        bar1 = slider_bars.findViewById(R.id.seekBar1);
        bar2 = slider_bars.findViewById(R.id.seekBar2);
        bar3 = slider_bars.findViewById(R.id.seekBar3);
        isSliding = false;

        bar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSliding)
                    applyProcessings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSliding = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSliding = false;
            }

        });
        bar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSliding)
                    applyProcessings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSliding = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSliding = false;
            }

        });
        bar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSliding)
                    applyProcessings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSliding = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSliding = false;
            }

        });

    }

    // Fonction qui permet de load les seekbars pour être plus efficace (éviter la redondance)
    public void seekbars_load(boolean visible1, String text1, int maxVal1, boolean visible2, String text2, int maxVal2, boolean visible3, String text3, int maxVal3) {
        TextView t1 = slider_bars.findViewById(R.id.textView1), t2 = slider_bars.findViewById(R.id.textView2), t3 = slider_bars.findViewById(R.id.textView3);
        bar1.setVisibility(visible1 ? View.VISIBLE : View.INVISIBLE);
        bar2.setVisibility(visible2 ? View.VISIBLE : View.INVISIBLE);
        bar3.setVisibility(visible3 ? View.VISIBLE : View.INVISIBLE);

        bar1.setMax(maxVal1);
        bar2.setMax(maxVal2);
        bar3.setMax(maxVal3);

        t1.setText(text1);
        t2.setText(text2);
        t3.setText(text3);

        buttons_view.removeAllViews();
        buttons_view.addView(slider_bars);
    }

    public void onClickAlgorithms(View v) {
        switch (v.getId()) {
            case R.id.gray_button:
                currentAlgorithm = AlgorithmType.GRAY;
                seekbars_load(true,"Red",100,true,"Green",100,true,"Blue",100);
                // Default bars
                bar1.setProgress(30);
                bar2.setProgress(59);
                bar3.setProgress(11);
                break;
            case R.id.random_button:
                currentAlgorithm = AlgorithmType.COLORIZE;
                seekbars_load(true,"Hue",359,false,"",1,false, "",1);
                bar1.setProgress((int) (Math.random() * 100));
                break;
            case R.id.selected_color_button:
                currentAlgorithm = AlgorithmType.COLOR_RANGE;
                seekbars_load(true,"Hue",359,true,"Chroma Key",180,false,"",1);
                break;
            case R.id.filter_button:
                //currentAlgorithm = AlgorithmType.CONVOLUTION;
                //seekbars_load(true,"Range",128,false,"",1,false,"",1);
                //bar1.setProgress(64);
                break;
            case R.id.linear_transformation_button:
                currentAlgorithm = AlgorithmType.DYNAMIC_EXTENSION;
                break;
            case R.id.egalisation_histogram_button:
                currentAlgorithm = AlgorithmType.HIST_EQUALIZER;
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
                Functions.toGray(processedBmp,bar1.getProgress()/100.0,bar2.getProgress()/100.0,bar3.getProgress()/100.0);
                break;
            case COLORIZE:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                Functions.colorize(processedBmp,bar1.getProgress());
                break;
            case COLOR_RANGE:
                processedBmp = currentBmp.copy(currentBmp.getConfig(),true);
                Functions.keepColor(processedBmp,bar1.getProgress(),bar2.getProgress());
                break;
            case DYNAMIC_EXTENSION:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                Contrast.linear_transformation(processedBmp);
                break;
            case HIST_EQUALIZER:
                processedBmp = currentBmp.copy(currentBmp.getConfig(),true);
                Contrast.histogramEqualizer(Tools.getHistogram(processedBmp),processedBmp);
                break;
            case NEGATIVE:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                Functions.negative(processedBmp);

        }
        imageView.setImageBitmap(processedBmp);
    }

    public void onClickReset(View v){
        currentBmp = savedBmp;
        processedBmp = savedBmp;
        imageView.setImageBitmap(savedBmp);
    }

    public void onClickReturn(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                currentAlgorithm = null;
                currentBmp = processedBmp;
                buttons_view.removeAllViews();
                buttons_view.addView(button_scroll);
        }
    }

}
