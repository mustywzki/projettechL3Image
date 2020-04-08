package com.mustywzki.projettechl3image.Algorithms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import com.mustywzki.projettechl3image.ScriptC_change_brightness;
import com.mustywzki.projettechl3image.ScriptC_change_saturation;
import com.mustywzki.projettechl3image.ScriptC_colorize2;
import com.mustywzki.projettechl3image.ScriptC_gray;
import com.mustywzki.projettechl3image.ScriptC_keepColor;
import com.mustywzki.projettechl3image.ScriptC_negative;

public class FunctionsRS extends Activity {

    public void toGrayRS(Context ctx, Bitmap bmp, double red_coef, double green_coef, double blue_coef){
        RenderScript rs = RenderScript.create(ctx) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs, bmp);
        Allocation output = Allocation.createTyped ( rs , input.getType()) ;
        // 3) Creer le script
        ScriptC_gray grayScript = new ScriptC_gray(rs);
        // 4) Copier les donnees dans les Allocations
        // ici inutile
        // 5) Initialiser les variables globales potentielles
        grayScript.set_newr(red_coef);
        grayScript.set_newg(green_coef);
        grayScript.set_newb(blue_coef);
        // 6) Lancer le noyau
        grayScript.forEach_toGray(input, output);
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }

    public void keepColorRS(Context ctx, Bitmap bmp, float hue, float chromakey) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_keepColor keepColorScript = new ScriptC_keepColor(rs);
        keepColorScript.set_hue(hue);
        keepColorScript.set_chromakey(chromakey);
        keepColorScript.forEach_keepColor(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        keepColorScript.destroy();
        rs.destroy();
    }



    public void colorize2(Context ctx, Bitmap bmp, float hue) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_colorize2 colorizeScript = new ScriptC_colorize2(rs);
        colorizeScript.set_rand(hue);
        colorizeScript.forEach_colorize(input,output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();

    }

    public void change_saturation(Context ctx, Bitmap bmp, float saturation) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_change_saturation SaturationScript = new ScriptC_change_saturation(rs);
        SaturationScript.set_saturation_change(saturation);
        SaturationScript.forEach_change_saturation(input,output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        SaturationScript.destroy();
        rs.destroy();

    }


    public void change_brightness(Context ctx, Bitmap bmp, float brightness) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_change_brightness BrightnessScript = new ScriptC_change_brightness(rs);
        BrightnessScript.set_brightness_change(brightness);
        BrightnessScript.forEach_change_brightness(input,output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        BrightnessScript.destroy();
        rs.destroy();

    }




    public void negative(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_negative NegativeScript = new ScriptC_negative(rs);
        NegativeScript.forEach_negative(input,output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        NegativeScript.destroy();
        rs.destroy();

    }




}
