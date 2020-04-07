
package com.mustywzki.projettechl3image;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mustywzki.projettechl3image.Algorithms.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int RESULT_LOAD_IMG=1000;
    private static final int PERMISSION_CODE = 1001;
    static final int RESULT_IMAGE_CAPTURE=1002;
    Uri image_uri = null;

    private View sliderBars, filterView, averageView, laplacienView, prewittView, sobelView;
    private Switch buttonSwitch;
    private FrameLayout buttonsView;
    private HorizontalScrollView buttonScroll;
    private SeekBar bar1, bar2, bar3;
    private boolean isSliding, isRenderscript;
    private AlgorithmType currentAlgorithm;
    private FunctionsRS functionsRS;

    private History history;

    private Button gray, keepColor;
    // GUI-related members
    private ImageView imageView;
    private Bitmap currentBmp, processedBmp, savedBmp;

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
        LAPLACIEN_8
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

        imageView = (ImageView)findViewById(R.id.picture);
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
                    ((TextView)findViewById(R.id.gray_text)).setTextColor(getResources().getColor(R.color.colorPrimary));
                    ((TextView)findViewById(R.id.keep_hue_text)).setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ((TextView)findViewById(R.id.gray_text)).setTextColor(getResources().getColor(R.color.colorAccent));
                    ((TextView)findViewById(R.id.keep_hue_text)).setTextColor(getResources().getColor(R.color.colorAccent));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);

            }
        }
    }

    /* --- OnClick functions --- */

    public void onClickReset(View v){
        currentBmp = savedBmp;
        processedBmp = savedBmp;
        imageView.setImageBitmap(savedBmp);
        history.addElement(currentBmp);
    }

    public void onClickReturn(View v) {
        buttonsView.removeAllViews();
        buttonsView.addView(buttonScroll);
        imageView.setImageBitmap(currentBmp);
    }

    public void onClickValidate(View v){
        buttonsView.removeAllViews();
        buttonsView.addView(buttonScroll);
        apply();
    }

    private void apply(){
        currentAlgorithm = null;
        currentBmp = processedBmp;
        history.addElement(currentBmp);
    }

    public void onClickReturnFilters(View v){
        currentAlgorithm = null;
        currentBmp = processedBmp;
        buttonsView.removeAllViews();
        buttonsView.addView(filterView);
    }

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

    public void onClickAlgorithms(View v) {
        switch (v.getId()) {
            case R.id.gray_button:
                currentAlgorithm = AlgorithmType.GRAY;
                seekbars_load("Gray", true,"Red",100,true,"Green",100,true,"Blue",100);
                // Default bars
                bar1.setProgress(30);
                bar2.setProgress(59);
                bar3.setProgress(11);
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
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
        applyProcessings();
    }

    /* --- Apply functions --- */

    public void applyProcessings(){
        processedBmp = currentBmp.copy(currentBmp.getConfig(),true);

        switch (currentAlgorithm){
            case GRAY:
                if (isRenderscript)
                    functionsRS.toGrayRS(getApplicationContext(), processedBmp, bar1.getProgress(), bar2.getProgress(), bar3.getProgress());
                else
                    Functions.toGray(processedBmp,bar1.getProgress()/100.0,bar2.getProgress()/100.0,bar3.getProgress()/100.0);
                break;
            case COLORIZE:
                if (isRenderscript)
                    functionsRS.colorize2(getApplicationContext(), processedBmp,bar1.getProgress() );

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
                Contrast.linear_transformation(processedBmp);
                apply();
                break;
            case HIST_EQUALIZER:
                Contrast.histogramEqualizer(Tools.getHistogram(processedBmp), processedBmp);
                apply();
                break;
            case NEGATIVE:
                Functions.negative(processedBmp);
                apply();
                break;
            case SATURATION:
                Functions.change_saturation(processedBmp,bar1.getProgress());
                break;
            case BRIGHTNESS:
                Functions.change_brightness(processedBmp,bar1.getProgress());
                break;
            case AVERAGE_3x3:
                Convolution.filter_Moyenneur(processedBmp, 9);
                break;
            case AVERAGE_7x7:
                Convolution.filter_Moyenneur(processedBmp, 49);
                break;
            case AVERAGE_15x15:
                Convolution.filter_Moyenneur(processedBmp, 225);
                break;
            case GAUSSIAN_5x5:
                Convolution.filter_Gaussien(processedBmp);
                break;
            case PREWITT_HOR:
                Convolution.filter_Prewitt_horizontal(processedBmp);
                break;
            case PREWITT_VER:
                Convolution.filter_Prewitt_vertical(processedBmp);
                break;
            case PREWITT_ALL:
                Convolution.filter_Prewitt(processedBmp);
                break;
            case SOBEL_HOR:
                Convolution.filter_Sobel_horizontal(processedBmp);
                break;
            case SOBEL_VER:
                Convolution.filter_Sobel_vertical(processedBmp);
                break;
            case SOBEL_ALL:
                Convolution.filter_Sobel(processedBmp);
                break;
            case LAPLACIEN_4:
                Convolution.filter_Laplacier_4(processedBmp);
                break;
            case LAPLACIEN_8:
                Convolution.filter_Laplacier_8(processedBmp);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentAlgorithm);
        }
        imageView.setImageBitmap(processedBmp);
    }

    /* --- SeekBar --- */

    public void setSeekBar(){
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

    public void seekbars_load(String name, boolean visible1, String text1, int maxVal1, boolean visible2, String text2, int maxVal2, boolean visible3, String text3, int maxVal3) {
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

            }
        }
        imageView.setImageURI(image_uri);
        currentBmp = savedBmp;
        processedBmp = savedBmp;
    }

    public void getImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }

    public void launchCamera(){
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put (MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, RESULT_IMAGE_CAPTURE);
    }

    /* --- Menu --- */

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.gallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        getImageFromGallery();
                    }

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        launchCamera();
                    }

                }
                else{
                    launchCamera();
                }
                break;
            case R.id.save:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                }

                String root = Environment.getExternalStorageDirectory().toString();
                System.out.println(root);
                File myDir = new File(root + "/piceditor");
                myDir.mkdirs();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fname = "PicEditor_" + timeStamp + ".jpg";

                File file = new File(myDir, fname);
                if (file.exists()) file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    processedBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,"Image saved", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}