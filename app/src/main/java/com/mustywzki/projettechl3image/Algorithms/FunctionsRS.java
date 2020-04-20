package com.mustywzki.projettechl3image.Algorithms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;

import com.mustywzki.projettechl3image.ScriptC_Gray;
import com.mustywzki.projettechl3image.ScriptC_HistogramEqualizer;
import com.mustywzki.projettechl3image.ScriptC_Max_Min;
import com.mustywzki.projettechl3image.ScriptC_apply_filter;
import com.mustywzki.projettechl3image.ScriptC_change_brightness;
import com.mustywzki.projettechl3image.ScriptC_change_saturation;
import com.mustywzki.projettechl3image.ScriptC_colorize;
import com.mustywzki.projettechl3image.ScriptC_histogramm;
import com.mustywzki.projettechl3image.ScriptC_keepcolor;
import com.mustywzki.projettechl3image.ScriptC_linear_extention;
import com.mustywzki.projettechl3image.ScriptC_mix_bmp;
import com.mustywzki.projettechl3image.ScriptC_negative;
import com.mustywzki.projettechl3image.ScriptC_set_rgb;

public class FunctionsRS extends Activity {

    public void toGrayRS(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        // 3) Creer le script
        ScriptC_Gray grayScript = new ScriptC_Gray(rs);
        // 4) Copier les donnees dans les Allocations
        // 5) Initialiser les variables globales potentielles
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

    public void setRgbRS(Context ctx, Bitmap bmp, float red_coef, float green_coef, float blue_coef) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_set_rgb setRgbScript = new ScriptC_set_rgb(rs);
        setRgbScript.set_red_coef(red_coef);
        setRgbScript.set_green_coef(green_coef);
        setRgbScript.set_blue_coef(blue_coef);
        setRgbScript.forEach_setRgb(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        setRgbScript.destroy();
        rs.destroy();
    }

    public void apply_filter(Context ctx, Bitmap bmp, float[] core, int core_length, int div) {

        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        Allocation core_tab = Allocation.createSized(rs, Element.F32(rs), core_length);
        core_tab.copyFrom(core);
        ScriptC_apply_filter apf_script = new ScriptC_apply_filter(rs);
        apf_script.set_width(bmp.getWidth());
        apf_script.set_height(bmp.getHeight());
        apf_script.set_core_length(core_length);
        apf_script.bind_core(core_tab);

        apf_script.set_div(div);
        apf_script.set_gIn(input);
        apf_script.forEach_apply_filter(input,output);
        output.copyTo(bmp);
        output.destroy();
        core_tab.destroy();
        input.destroy();
        apf_script.destroy();
        rs.destroy();

    }

    public void mix_bmp (Context ctx,Bitmap bmp, Bitmap bmp1, Bitmap bmp2){
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation  A1= Allocation.createFromBitmap(rs, bmp1);
        Allocation A2 = Allocation.createFromBitmap(rs, bmp2);
        Allocation output = Allocation.createTyped(rs, A1.getType());
        ScriptC_mix_bmp Mbmp_Script = new ScriptC_mix_bmp(rs);
        Mbmp_Script.set_A1(A1);
        Mbmp_Script.set_A2(A2);
        Mbmp_Script.forEach_mix_bmp(input, output);
        output.copyTo(bmp);
        output.destroy();
        A1.destroy();
        A2.destroy();
        input.destroy();
        output.destroy();
        Mbmp_Script.destroy();
        rs.destroy();


    }


}


