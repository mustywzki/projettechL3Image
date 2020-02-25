package com.mustywzki.projettechl3image;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mustywzki.projettechl3image.Algorithms.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE=1;
    static final int RESULT_LOAD_IMG=1000;
    private static final int PERMISSION_CODE = 1001;
    Uri image_uri = null;

    private View slider_bars, filter_view, average_view, laplacien_view, prewitt_view, sobel_view;
    private Switch switchbutton;
    private FrameLayout buttons_view;
    private HorizontalScrollView button_scroll;
    private SeekBar bar1, bar2, bar3;
    private boolean isSliding, isRenderscript;
    private AlgorithmType currentAlgorithm;
    private FunctionsRS functionsRS;

    // GUI-related members
    private ImageView imageView;
    private Bitmap currentBmp, processedBmp, savedBmp;
    Button camera_button, gallery_button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestpermissions();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE);

        slider_bars = View.inflate(this,R.layout.seekbar_view,null);
        filter_view = View.inflate(this, R.layout.filter_view, null);
        average_view = View.inflate(this, R.layout.average_filter_view, null);
        laplacien_view = View.inflate(this, R.layout.laplacien_filter_view, null);
        prewitt_view = View.inflate(this, R.layout.prewitt_filter_view, null);
        sobel_view = View.inflate(this, R.layout.sobel_filter_view, null);

        imageView = findViewById(R.id.picture);
        button_scroll = findViewById(R.id.button_scroll);
        buttons_view = findViewById(R.id.button_view);
        switchbutton = findViewById(R.id.renderscript_switch);

        functionsRS = new FunctionsRS();

        currentBmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        savedBmp = currentBmp;
        processedBmp = currentBmp;

        setSeekBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void requestpermissions() {
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
    }

    public void onClickReturn(View v) {
        currentAlgorithm = null;
        currentBmp = processedBmp;
        buttons_view.removeAllViews();
        buttons_view.addView(button_scroll);
    }

    public void onClickSave() {
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
    }

    public void onClickCamera(){
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
    }

    public void onClickGallery (){
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

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            camera_button.setEnabled(false);
        }
    }

    public void onClickReturnFilters(View v){
        currentAlgorithm = null;
        currentBmp = processedBmp;
        buttons_view.removeAllViews();
        buttons_view.addView(filter_view);
    }

    public void onClickView(View v){
        switch (v.getId()){
            case R.id.filter_button:
                buttons_view.removeAllViews();
                buttons_view.addView(filter_view);
                break;
            case R.id.average_button:
                buttons_view.removeAllViews();
                buttons_view.addView(average_view);
                break;
            case R.id.prewitt_button:
                buttons_view.removeAllViews();
                buttons_view.addView(prewitt_view);
                break;
            case R.id.sobel_button:
                buttons_view.removeAllViews();
                buttons_view.addView(sobel_view);
                break;
            case R.id.laplacian_button:
                buttons_view.removeAllViews();
                buttons_view.addView(laplacien_view);
                break;
        }
    }

    public void onClickAlgorithms(View v) {
        isRenderscript = switchbutton.isChecked();
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
                seekbars_load(true,"Hue",100,false,"",1,false, "",1);
                bar1.setProgress(50);
                break;
            case R.id.brightness_button:
                currentAlgorithm = AlgorithmType.BRIGHTNESS;
                seekbars_load(true,"Hue",100,false,"",1,false, "",1);
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
                    functionsRS.toGrayRS(getApplicationContext(), processedBmp,bar1.getProgress(),bar2.getProgress(),bar3.getProgress());
                else
                    Functions.toGray(processedBmp,bar1.getProgress()/100.0,bar2.getProgress()/100.0,bar3.getProgress()/100.0);
                break;
            case COLORIZE:
                Functions.colorize(processedBmp,bar1.getProgress());
                break;
            case COLOR_RANGE:
                if (isRenderscript)
                    functionsRS.keepColorRS(getApplicationContext(), processedBmp,bar1.getProgress(),bar2.getProgress());
                else
                    Functions.keepColor(processedBmp,bar1.getProgress(),bar2.getProgress());
                break;
            case DYNAMIC_EXTENSION:
                Contrast.linear_transformation(processedBmp);
                break;
            case HIST_EQUALIZER:
                break;
            case NEGATIVE:
                Functions.negative(processedBmp);
                currentBmp = processedBmp;
                currentAlgorithm = null;
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

    /* --- Camera and Gallery --- */

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data!=null) {
            try {
                    savedBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        else if(requestCode == RESULT_LOAD_IMG && data!=null && resultCode==RESULT_OK){
            Uri uri = data.getData();
            try{
                savedBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            }
            catch (Exception e){


            }
        }
        imageView.setImageURI(image_uri);
        currentBmp = savedBmp;
        processedBmp = savedBmp;
        imageView.setImageBitmap(savedBmp);
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
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    /* --- Menu --- */

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.gallery:
                onClickGallery();
                break;
            case R.id.camera:
                onClickCamera();
                break;
            case R.id.save:
                onClickSave();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
