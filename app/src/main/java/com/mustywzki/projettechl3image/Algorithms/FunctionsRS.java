package com.mustywzki.projettechl3image.Algorithms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import com.mustywzki.projettechl3image.ScriptC_Gray;
import com.mustywzki.projettechl3image.ScriptC_HistogramEqualizer;
import com.mustywzki.projettechl3image.ScriptC_Max_Min;
import com.mustywzki.projettechl3image.ScriptC_change_brightness;
import com.mustywzki.projettechl3image.ScriptC_change_saturation;
import com.mustywzki.projettechl3image.ScriptC_colorize;
import com.mustywzki.projettechl3image.ScriptC_histogramm;
import com.mustywzki.projettechl3image.ScriptC_keepcolor;
import com.mustywzki.projettechl3image.ScriptC_linear_extention;
import com.mustywzki.projettechl3image.ScriptC_negative;

public class FunctionsRS extends Activity {

    //TODO doesn't change with seekbar
    public void toGrayRS(Context ctx, Bitmap bmp, float red_coef, float green_coef, float blue_coef) {
        RenderScript rs = RenderScript.create(ctx);
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        // 3) Creer le script
        ScriptC_Gray grayScript = new ScriptC_Gray(rs);
        // 4) Copier les donnees dans les Allocations
        // ici inutile
        // 5) Initialiser les variables globales potentielles
        grayScript.set_red_coef(red_coef);
        grayScript.set_green_coef(green_coef);
        grayScript.set_blue_coef(blue_coef);
        // 6) Lancer le noyau
        grayScript.forEach_Gray(input, output);
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }


    public void keepColorRS(Context ctx, Bitmap bmp, float hue, float tolerance) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_keepcolor keepColorScript = new ScriptC_keepcolor(rs);
        keepColorScript.set_hue(hue);
        keepColorScript.set_tolerance(tolerance);
        keepColorScript.forEach_keepcolor(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        keepColorScript.destroy();
        rs.destroy();
    }

    public void colorize(Context ctx, Bitmap bmp, float hue) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs);
        colorizeScript.set_rand(hue);
        colorizeScript.forEach_colorize(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();

    }

    public void change_saturation(Context ctx, Bitmap bmp, float saturation) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_change_saturation SaturationScript = new ScriptC_change_saturation(rs);
        SaturationScript.set_saturation_change(saturation);
        SaturationScript.forEach_change_saturation(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        SaturationScript.destroy();
        rs.destroy();

    }

    public void change_brightness(Context ctx, Bitmap bmp, float brightness) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_change_brightness BrightnessScript = new ScriptC_change_brightness(rs);
        BrightnessScript.set_brightness_change(brightness);
        BrightnessScript.forEach_change_brightness(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        BrightnessScript.destroy();
        rs.destroy();

    }

    public void negative(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_negative NegativeScript = new ScriptC_negative(rs);
        NegativeScript.forEach_negative(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        NegativeScript.destroy();
        rs.destroy();

    }

    int [] histogramm(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        ScriptC_histogramm HistogrammScript = new ScriptC_histogramm(rs);
        int[] histo =  HistogrammScript.reduce_histogramm(input).get();

        input.destroy();
        HistogrammScript.destroy();
        rs.destroy();
        return Tools.cumulativeHistogram(histo);
    }


    public void HistogramEqualizer(Context ctx, Bitmap bmp) {
        int[] histo = histogramm(ctx,bmp);
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_HistogramEqualizer HEScript= new ScriptC_HistogramEqualizer(rs);
        HEScript.set_cumulative_histogramm(histo);

        HEScript.set_tab_length(bmp.getWidth()*bmp.getHeight());
        HEScript.forEach_HistogramEqualize(input, output);

        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        HEScript.destroy();
        rs.destroy();

    }

    int[] max_min(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        ScriptC_Max_Min max_minScript = new ScriptC_Max_Min(rs);
        int[] maxi_mini = max_minScript.reduce_MaxMin(input).get();
        input.destroy();
        max_minScript.destroy();
        rs.destroy();
        return maxi_mini;
    }

    public void LinearExtention(Context ctx, Bitmap bmp) {
        int[] tab = max_min(ctx, bmp);
        int[] LUTred = Contrast.createLUTred(new int[]{tab[0], tab[1]});
        int[] LUTgreen = Contrast.createLUTgreen(new int[]{tab[2],tab[3]});
        int[] LUTblue = Contrast.createLUTblue(new int[]{tab[4],tab[5]});

        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_linear_extention LeScript= new ScriptC_linear_extention(rs);
        LeScript.set_LUTred(LUTred);
        LeScript.set_LUTgreen(LUTgreen);
        LeScript.set_LUTblue(LUTblue);
        LeScript.forEach_linear_extention(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        LeScript.destroy();
        rs.destroy();

    }





}


