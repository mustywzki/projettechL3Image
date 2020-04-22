
package com.mustywzki.projettechl3image;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mustywzki.projettechl3image.algorithms.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG=1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int RESULT_IMAGE_CAPTURE=1002;
    private Uri image_uri = null;

    private View sliderBars, filterView, averageView, laplacienView, prewittView, sobelView, blurView, transformationView;
    private Switch buttonSwitch;
    private FrameLayout buttonsView;
    private HorizontalScrollView buttonScroll;
    private SeekBar bar1, bar2, bar3;
    private boolean isSliding, isRenderscript;
    private AlgorithmType currentAlgorithm;
    private FunctionsRS functionsRS;
    private final ArrayList<TextView> rsTexts = new ArrayList<>();

    private History history;

    // GUI-related members
    private ImageView imageView;
    private Bitmap currentBmp, processedBmp, savedBmp;

    /**
     * Enum listing all types of processing done to the target image.
     */
    public enum AlgorithmType {
        GRAY,
        COLORIZE,
        COLOR_RANGE,
        DYNAMIC_EXTENSION,
        HIST_EQUALIZER,
        NEGATIVE,
        SATURATION,
        BRIGHTNESS,
        AVERAGE_3x3,
        AVERAGE_7x7,
        AVERAGE_15x15,
        GAUSSIAN_5x5,
        PREWITT_HOR,
        PREWITT_VER,
        PREWITT_ALL,
        SOBEL_HOR,
        SOBEL_VER,
        SOBEL_ALL,
        LAPLACIEN_4,
        LAPLACIEN_8,
        SETRGB,
        REVERSEVER,
        REVERSEHOR,
        ROTATELEFT,
        ROTATERIGHT,
        SEPIA
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
        //        View.SYSTEM_UI_FLAG_FULLSCREEN |
        //        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
        //        View.SYSTEM_UI_FLAG_IMMERSIVE);

        sliderBars = View.inflate(this,R.layout.slider_view,null);
        filterView = View.inflate(this, R.layout.filter_view, null);
        averageView = View.inflate(this, R.layout.average_filter_view, null);
        laplacienView = View.inflate(this, R.layout.laplacien_filter_view, null);
        prewittView = View.inflate(this, R.layout.prewitt_filter_view, null);
        sobelView = View.inflate(this, R.layout.sobel_filter_view, null);
        blurView = View.inflate(this, R.layout.blur_view, null);
        transformationView = View.inflate(this, R.layout.transformation_view, null);

        rsTexts.add((TextView) findViewById(R.id.keep_hue_text));
        rsTexts.add((TextView) findViewById(R.id.brightness_text));
        rsTexts.add((TextView) findViewById(R.id.saturation_text));
        rsTexts.add((TextView) findViewById(R.id.negative_text));
        rsTexts.add((TextView) findViewById(R.id.colorise_text));
        rsTexts.add((TextView) findViewById(R.id.setRgb_text));
        rsTexts.add((TextView) findViewById(R.id.blur_text));
        rsTexts.add((TextView) findViewById(R.id.transformation_text));
        rsTexts.add((TextView) findViewById(R.id.filter_text));

        rsTexts.add((TextView) filterView.findViewById(R.id.gray_text));
        rsTexts.add((TextView) filterView.findViewById(R.id.linear_transformation_text));
        rsTexts.add((TextView) filterView.findViewById(R.id.egalisation_text));
        rsTexts.add((TextView) filterView.findViewById(R.id.laplacian_text));
        rsTexts.add((TextView) filterView.findViewById(R.id.sepia_text));
        rsTexts.add((TextView) filterView.findViewById(R.id.prewitt_text));
        rsTexts.add((TextView) filterView.findViewById(R.id.sobel_text));

        rsTexts.add((TextView) prewittView.findViewById(R.id.prewitt_hor_text));
        rsTexts.add((TextView) prewittView.findViewById(R.id.prewitt_ver_text));
        rsTexts.add((TextView) prewittView.findViewById(R.id.prewitt_all_text));

        rsTexts.add((TextView) sobelView.findViewById(R.id.sobel_hor_text));
        rsTexts.add((TextView) sobelView.findViewById(R.id.sobel_ver_text));
        rsTexts.add((TextView) sobelView.findViewById(R.id.sobel_all_text));

        rsTexts.add((TextView) laplacienView.findViewById(R.id.laplacian_4_text));
        rsTexts.add((TextView) laplacienView.findViewById(R.id.laplacian_8_text));

        rsTexts.add((TextView) transformationView.findViewById(R.id.mirror_text));
        rsTexts.add((TextView) transformationView.findViewById(R.id.rotation_text));

        rsTexts.add((TextView) blurView.findViewById(R.id.gaussian_text));
        rsTexts.add((TextView) blurView.findViewById(R.id.average_text));

        rsTexts.add((TextView) averageView.findViewById(R.id.average_3_text));
        rsTexts.add((TextView) averageView.findViewById(R.id.average_7_text));
        rsTexts.add((TextView) averageView.findViewById(R.id.average_15_text));

        imageView = findViewById(R.id.picture);
        PhotoViewAttacher photoView = new PhotoViewAttacher(imageView);
        photoView.update();
        buttonScroll = findViewById(R.id.button_scroll);
        buttonsView = findViewById(R.id.button_view);
        buttonSwitch = findViewById(R.id.renderscript_switch);
        functionsRS = new FunctionsRS();
        currentBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        savedBmp = currentBmp;
        processedBmp = currentBmp;
        history = new History(10, currentBmp);

        setButtonSwitch();
        setSeekBar();
    }

    private void setButtonSwitch(){
        buttonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRenderscript = isChecked;
                if (isRenderscript) {
                    for (int i = 0; i < rsTexts.size(); i++){
                        rsTexts.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    }
                } else {
                    for (int i = 0; i < rsTexts.size(); i++){
                        rsTexts.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void requestPermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, PERMISSION_CODE);

        }
    }

    /* --- OnClick functions --- */

    /**
     * onClick function which indicates when to reset the initial image.
     * @param v Applied View for the Reset
     */
    public void onClickReset(View v){
        currentBmp = savedBmp;
        processedBmp = savedBmp;
        imageView.setImageBitmap(savedBmp);
        history.addElement(currentBmp);
        Toast toast = Toast.makeText(this,"Image reset", Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.color.background);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        toast.show();
    }

    /**
     * onClick function which indicates when to go backwards from one action.
     * @param v Applied View for the Return
     */
    public void onClickReturn(View v) {
        buttonsView.removeAllViews();
        switch (v.getId()){
            case R.id.backButtonFilter:
                System.out.println("HERE");
                buttonsView.addView(filterView);
                break;
            case R.id.backButtonBlur:
                buttonsView.addView(blurView);
                break;
            default:
                buttonsView.addView(buttonScroll);
                break;
        }
        imageView.setImageBitmap(currentBmp);
    }

    /**
     * onClick function which performs the algorithm if it has been validated.
     * @param v Applied view for the validate
     */
    public void onClickValidate(View v){
        buttonsView.removeAllViews();
        buttonsView.addView(buttonScroll);
        apply();
    }

    /**
     * Save the previous image in the history in order to keep track of the previous image version.
     */
    private void apply(){
        currentAlgorithm = null;
        currentBmp = processedBmp;
        history.addElement(currentBmp);
    }

    /**
     * When clicking on buttons indiccating a modification, performs it.
     * @param v View in which buttons are clickable and valid
     */
    public void onClickView(View v){
        switch (v.getId()){
            case R.id.filter_button:
                buttonsView.removeAllViews();
                buttonsView.addView(filterView);
                break;
            case R.id.average_button:
                buttonsView.removeAllViews();
                buttonsView.addView(averageView);
                break;
            case R.id.prewitt_button:
                buttonsView.removeAllViews();
                buttonsView.addView(prewittView);
                break;
            case R.id.sobel_button:
                buttonsView.removeAllViews();
                buttonsView.addView(sobelView);
                break;
            case R.id.laplacian_button:
                buttonsView.removeAllViews();
                buttonsView.addView(laplacienView);
                break;
            case R.id.blur_button:
                buttonsView.removeAllViews();
                buttonsView.addView(blurView);
                break;
            case R.id.transform_button:
                buttonsView.removeAllViews();
                buttonsView.addView(transformationView);
        }
    }

    public void onClickPrevious(View v){
        if (history.getIndCurPicture() > 0){
            history.setIndCurPicture(history.getIndCurPicture() -1);
            currentBmp = history.getCur_picture();
            imageView.setImageBitmap(currentBmp);

        }
    }

    public void onClickNext(View v){
        if (history.getIndCurPicture() < history.getTop()){
            history.setIndCurPicture(history.getIndCurPicture() +1);
            currentBmp = history.getCur_picture();
            imageView.setImageBitmap(currentBmp);
        }
    }

    /**
     * Generate the view that will be shown for each algorithm.
     * @param v View in which buttons are clickable and valid
     */
    public void onClickAlgorithms(View v) {
        switch (v.getId()) {
            case R.id.gray_button:
                currentAlgorithm = AlgorithmType.GRAY;
                break;
            case R.id.random_button:
                currentAlgorithm = AlgorithmType.COLORIZE;
                seekbars_load("Colorise", true,"Hue",359,false,"",1,false, "",1);
                bar1.setProgress((int) (Math.random() * 100));
                break;
            case R.id.selected_color_button:
                currentAlgorithm = AlgorithmType.COLOR_RANGE;
                seekbars_load("Keep Hue", true,"Hue",359,true,"Tolerance",180,false,"",1);
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
            case R.id.saturation_button:
                currentAlgorithm = AlgorithmType.SATURATION;
                seekbars_load("Saturation", true,"Hue",100,false,"",1,false, "",1);
                bar1.setProgress(50);
                break;
            case R.id.brightness_button:
                currentAlgorithm = AlgorithmType.BRIGHTNESS;
                seekbars_load("Brightness", true,"Hue",100,false,"",1,false, "",1);
                bar1.setProgress(50);
                break;
            case R.id.average_3_button:
                currentAlgorithm = AlgorithmType.AVERAGE_3x3;
                break;
            case R.id.average_7_button:
                currentAlgorithm = AlgorithmType.AVERAGE_7x7;
                break;
            case R.id.average_15_button:
                currentAlgorithm = AlgorithmType.AVERAGE_15x15;
                break;
            case R.id.gaussian_button:
                currentAlgorithm = AlgorithmType.GAUSSIAN_5x5;
                break;
            case R.id.prewitt_hor_button:
                currentAlgorithm = AlgorithmType.PREWITT_HOR;
                break;
            case R.id.prewitt_ver_button:
                currentAlgorithm = AlgorithmType.PREWITT_VER;
                break;
            case R.id.prewitt_all_button:
                currentAlgorithm = AlgorithmType.PREWITT_ALL;
                break;
            case R.id.sobel_hor_button:
                currentAlgorithm = AlgorithmType.SOBEL_HOR;
                break;
            case R.id.sobel_ver_button:
                currentAlgorithm = AlgorithmType.SOBEL_VER;
                break;
            case R.id.sobel_all_button:
                currentAlgorithm = AlgorithmType.SOBEL_ALL;
                break;
            case R.id.laplacian_4_button:
                currentAlgorithm = AlgorithmType.LAPLACIEN_4;
                break;
            case R.id.laplacian_8_button:
                currentAlgorithm = AlgorithmType.LAPLACIEN_8;
                break;
            case R.id.setRGB_button:
                currentAlgorithm = AlgorithmType.SETRGB;
                seekbars_load("Set RGB", true,"Red",100,true,"Green",100,true,"Blue",100);
                // Default bars
                bar1.setProgress(50);
                bar2.setProgress(50);
                bar3.setProgress(50);
                break;
            case R.id.reverseVerButton:
                currentAlgorithm = AlgorithmType.REVERSEVER;
                break;
            case R.id.reverseHorButton:
                currentAlgorithm = AlgorithmType.REVERSEHOR;
                break;
            case R.id.rotate_left_button:
                    currentAlgorithm = AlgorithmType.ROTATELEFT;

                break;
            case R.id.rotate_right_button:
                currentAlgorithm = AlgorithmType.ROTATERIGHT;
                break;
            case R.id.sepia_button:
                currentAlgorithm = AlgorithmType.SEPIA;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
        applyProcessings();
    }

    /* --- Apply functions --- */

    /**
     * Apply algorithm depending on which buttons has been pressed and if RenderScript is activated or not.
     */
    private void applyProcessings(){
        processedBmp = currentBmp.copy(currentBmp.getConfig(),true);

        switch (currentAlgorithm){
            case GRAY:
                if (isRenderscript)
                    functionsRS.toGrayRS(getApplicationContext(), processedBmp);
                else
                    Functions.toGray(processedBmp);
                apply();
                break;
            case COLORIZE:
                if (isRenderscript)
                    functionsRS.colorize(getApplicationContext(), processedBmp,bar1.getProgress());
                else
                    Functions.colorize(processedBmp,bar1.getProgress());
                break;
            case COLOR_RANGE:
                if (isRenderscript)
                    functionsRS.keepColorRS(getApplicationContext(), processedBmp, bar1.getProgress(), bar2.getProgress());
                else
                    Functions.keepColor(processedBmp,bar1.getProgress(),bar2.getProgress());
                break;
            case DYNAMIC_EXTENSION:
                if(isRenderscript)
                    functionsRS.LinearExtention(getApplicationContext(),processedBmp);
                else
                    Contrast.linear_transformation(processedBmp);
                apply();
                break;
            case HIST_EQUALIZER:
                if (isRenderscript)
                    functionsRS.HistogramEqualizer(getApplicationContext(),processedBmp);
                else
                    Contrast.histogramEqualizer(Tools.getHistogram(processedBmp), processedBmp);
                apply();
                break;
            case NEGATIVE:
                if (isRenderscript)
                    functionsRS.negative(getApplicationContext(),processedBmp);
                else
                    Functions.negative(processedBmp);
                apply();
                break;
            case SATURATION:
                if (isRenderscript)
                    functionsRS.change_saturation(getApplicationContext(),processedBmp, bar1.getProgress());
                else
                    Functions.change_saturation(processedBmp,bar1.getProgress());
                break;
            case BRIGHTNESS:
                if (isRenderscript)
                    functionsRS.change_brightness(getApplicationContext(),processedBmp, (float)bar1.getProgress());
                else
                    Functions.change_brightness(processedBmp,bar1.getProgress());
                break;
            case AVERAGE_3x3:
                if(isRenderscript){
                    float[] core = new float[9];
                    for (int i = 0; i < core.length; i++){
                        core[i] = 1;
                    }
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 9);
                }
                else
                    Convolution.filter_Moyenneur(processedBmp, 9);
                break;
            case AVERAGE_7x7:
                if(isRenderscript){
                    float[] core = new float[49];
                    for (int i = 0; i < core.length; i++){
                        core[i] = 1;
                    }
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 49);
                }
                else
                    Convolution.filter_Moyenneur(processedBmp, 49);
                break;
            case AVERAGE_15x15:
                if(isRenderscript){
                    float[] core = new float[225];
                    for (int i = 0; i < core.length; i++){
                        core[i] = 1;
                    }
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 225);
                }
                else
                    Convolution.filter_Moyenneur(processedBmp, 225);
                break;
            case GAUSSIAN_5x5:
                if(isRenderscript){
                    float[] core = {1, 2, 3, 2, 1
                            ,2, 6, 8, 6, 2
                            ,3, 8, 10, 8, 3
                            ,2, 6, 8, 6, 2
                            ,1, 2, 3, 2, 1};
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 98);
                }
                else
                    Convolution.filter_Gaussien(processedBmp);
                apply();
                break;
            case PREWITT_HOR:
                if(isRenderscript){
                    float[] core = {-1, -1, -1
                            ,0, 0, 0
                            ,1, 1, 1};
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 1);
                }
                else
                    Convolution.filter_Prewitt_horizontal(processedBmp);
                break;
            case PREWITT_VER:
                if(isRenderscript){
                    float[] core = {-1, 0, 1
                            ,-1, 0, 1
                            ,-1, 0, 1};
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 1);
                }
                else
                    Convolution.filter_Prewitt_vertical(processedBmp);
                break;
            case PREWITT_ALL:
                if(isRenderscript) {
                    Bitmap b1 = processedBmp.copy(processedBmp.getConfig(), true);
                    Bitmap b2 = processedBmp.copy(processedBmp.getConfig(), true);
                    float[] core1 = {-1, -1, -1
                            , 0, 0, 0
                            , 1, 1, 1};
                    functionsRS.apply_filter(getApplicationContext(), b1, core1, core1.length, 1);
                    float[] core2 = {-1, 0, 1
                            , -1, 0, 1
                            , -1, 0, 1};
                    functionsRS.apply_filter(getApplicationContext(), b2, core2, core2.length, 1);
                    functionsRS.mix_bmp(getApplicationContext(), processedBmp, b1, b2);
                }
                else
                    Convolution.filter_Prewitt(processedBmp);
                break;
            case SOBEL_HOR:
                if(isRenderscript){
                    float[] core = {-1, -2, -1
                            ,0, 0, 0
                            ,1, 2, 1};
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 1);
                }
                else
                    Convolution.filter_Sobel_horizontal(processedBmp);
                break;
            case SOBEL_VER:
                if(isRenderscript){
                    float[] core = {-1, 0, 1
                            ,-2, 0, 2
                            ,-1, 0, 1};
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 1);
                }
                else
                    Convolution.filter_Sobel_vertical(processedBmp);
                break;
            case SOBEL_ALL:
                if(isRenderscript) {
                    Bitmap b1 = processedBmp.copy(processedBmp.getConfig(), true);
                    Bitmap b2 = processedBmp.copy(processedBmp.getConfig(), true);
                    float[] core1 = {-1, -2, -1
                            , 0, 0, 0
                            , 1, 2, 1};
                    functionsRS.apply_filter(getApplicationContext(), b1, core1, core1.length, 1);
                    float[] core2 = {-1, 0, 1
                            , -2, 0, 2
                            , -1, 0, 1};
                    functionsRS.apply_filter(getApplicationContext(), b2, core2, core2.length, 1);
                    functionsRS.mix_bmp(getApplicationContext(),processedBmp,b1,b2);
                }
                else
                    Convolution.filter_Sobel(processedBmp);
                break;
            case LAPLACIEN_4:
                if(isRenderscript){
                    float[] core = {0, 1, 0
                            ,1, -4, 1
                            ,0, 1, 0};
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 1);
                }
                else
                    Convolution.filter_Laplacier_4(processedBmp);
                break;
            case LAPLACIEN_8:
                if(isRenderscript){
                    float[] core = {1, 1, 1
                            ,1, -8, 1
                            ,1, 1, 1};
                    functionsRS.apply_filter(getApplicationContext(), processedBmp, core, core.length, 1);
                }
                else
                    Convolution.filter_Laplacier_8(processedBmp);
                break;
            case SETRGB:
                if (isRenderscript)
                    functionsRS.setRgbRS(getApplicationContext(), processedBmp, bar1.getProgress(), bar2.getProgress(),  bar3.getProgress());
                else
                    Functions.setRGB(processedBmp, bar1.getProgress(), bar2.getProgress(),  bar3.getProgress());
                break;
            case REVERSEVER:
                if(isRenderscript)
                    functionsRS.reverseVer(getApplicationContext(), processedBmp);
                else
                    Functions.reverseVer(processedBmp);
                apply();
                break;
            case REVERSEHOR:
                if(isRenderscript)
                    functionsRS.reverseHor(getApplicationContext(),processedBmp);
                else
                    Functions.reverseHor(processedBmp);
                apply();
                break;
            case ROTATELEFT:
                if(isRenderscript)
                    processedBmp = functionsRS.rotateLeft(getApplicationContext(),processedBmp);
                else
                    processedBmp = Functions.rotateLeft(processedBmp);
                apply();
                break;
            case ROTATERIGHT:
                if(isRenderscript)
                    processedBmp = functionsRS.rotateRight(getApplicationContext(),processedBmp);
                else
                    processedBmp = Functions.rotateRight(processedBmp);
                apply();
                break;
            case SEPIA:
                if(isRenderscript)
                    functionsRS.toSepiaRS(getApplicationContext(), processedBmp);
                else
                    Functions.toSepia(processedBmp);
                apply();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentAlgorithm);
        }
        imageView.setImageBitmap(processedBmp);
    }

    /* --- SeekBar --- */

    /**
     * Tool function which set up all seek bars on the app start up.
     */
    private void setSeekBar(){
        // Option sliders listeners
        bar1 = sliderBars.findViewById(R.id.seekBar1);
        bar2 = sliderBars.findViewById(R.id.seekBar2);
        bar3 = sliderBars.findViewById(R.id.seekBar3);
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


    /**
     * Tool function which helps setting up seekBars that we use for Algorithms views.
     * @param name indicator to see which seekBar it is
     * @param visible1 1st bar visibility
     * @param text1 1st bar text
     * @param maxVal1 1st bar max value settable
     * @param visible2 2nd bar visibility
     * @param text2 2nd bar text
     * @param maxVal2 2nd bar max value settable
     * @param visible3 3rd bar visibility
     * @param text3 3rd bar text
     * @param maxVal3 3rd bar max value settable
     */
    private void seekbars_load(String name, boolean visible1, String text1, int maxVal1, boolean visible2, String text2, int maxVal2, boolean visible3, String text3, int maxVal3) {
        TextView t1 = sliderBars.findViewById(R.id.textView1), t2 = sliderBars.findViewById(R.id.textView2), t3 = sliderBars.findViewById(R.id.textView3), t4 = sliderBars.findViewById(R.id.textView4);
        bar1.setVisibility(visible1 ? View.VISIBLE : View.GONE);
        bar2.setVisibility(visible2 ? View.VISIBLE : View.GONE);
        bar3.setVisibility(visible3 ? View.VISIBLE : View.GONE);

        bar1.setMax(maxVal1);
        bar2.setMax(maxVal2);
        bar3.setMax(maxVal3);

        t1.setText(text1);
        t2.setText(text2);
        t3.setText(text3);
        t4.setText(name);

        buttonsView.removeAllViews();
        buttonsView.addView(sliderBars);
    }

    /* --- Camera and Gallery --- */

    /**
     * Retrieve image from Camera and/or Gallery.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            try {
                savedBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == RESULT_LOAD_IMG && data!=null && resultCode==RESULT_OK){
            image_uri = data.getData();
            try{
                savedBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        imageView.setImageURI(image_uri);
        currentBmp = savedBmp;
        processedBmp = savedBmp;
        history.reset(currentBmp);
    }

    /**
     * Get Image from Gallery and load onActivityResult if succeed
     */
    private void getImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }

    /**
     * Launch Camera and wait for onActivityResult if succeed
     */
    private void launchCamera(){
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put (MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, RESULT_IMAGE_CAPTURE);
    }

    /* --- Menu --- */

    /**
     * Function that detects which button is selected between either Camera Roll or Gallery or saving a file.
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.gallery:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    getImageFromGallery();
                }

                if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                    Button cameraButton = findViewById(R.id.gallery);
                    cameraButton.setEnabled(false);
                }
                break;
            case R.id.camera:
                if (checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    launchCamera();
                }
                break;
            case R.id.save:
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                String dateTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                if(SDK_INT > Build.VERSION_CODES.P) {
                    System.out.println("HERE");
                    MediaStore.Images.Media.insertImage(getContentResolver(), processedBmp, "PicEd_"+dateTime+".jpg", "");
                }
                else{
                    String path = Environment.getExternalStorageDirectory().toString();
                    path = path.concat("/Pictures/PicEditor");

                    File myDir = new File(path);
                    myDir.mkdirs();

                    System.out.println(path);
                    OutputStream fOut;

                    String filename = "PicEd_"+dateTime+".jpg";
                    File file = new File(path, filename); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

                    try {
                        fOut = new FileOutputStream(file);
                        Bitmap pictureBitmap = processedBmp.copy(processedBmp.getConfig(), true);
                        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                        fOut.close(); // do not forget to close the stream
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    galleryAdd(path.concat("/").concat(filename));
                }
                Toast toast = Toast.makeText(this,"Image saved in Pictures/PicEditor", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.setBackgroundResource(R.color.background);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                toast.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void galleryAdd(String photopath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photopath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}