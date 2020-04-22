package com.mustywzki.projettechl3image.algorithms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;

import com.mustywzki.projettechl3image.ScriptC_Apply_Filter;
import com.mustywzki.projettechl3image.ScriptC_Change_Brightness;
import com.mustywzki.projettechl3image.ScriptC_Change_Saturation;
import com.mustywzki.projettechl3image.ScriptC_Colorize;
import com.mustywzki.projettechl3image.ScriptC_Gray;
import com.mustywzki.projettechl3image.ScriptC_Histogram;
import com.mustywzki.projettechl3image.ScriptC_Histogram_Egalisation;
import com.mustywzki.projettechl3image.ScriptC_KeepColor;
import com.mustywzki.projettechl3image.ScriptC_Linear_Extention;
import com.mustywzki.projettechl3image.ScriptC_Max_Min;
import com.mustywzki.projettechl3image.ScriptC_MixBmp;
import com.mustywzki.projettechl3image.ScriptC_Negative;
import com.mustywzki.projettechl3image.ScriptC_ReverseHor;
import com.mustywzki.projettechl3image.ScriptC_ReverseVer;
import com.mustywzki.projettechl3image.ScriptC_RotateLeft;
import com.mustywzki.projettechl3image.ScriptC_RotateRight;
import com.mustywzki.projettechl3image.ScriptC_Sepia;
import com.mustywzki.projettechl3image.ScriptC_SetRGB;


public class FunctionsRS extends Activity {

    /**
     * Function that creates an image of shades of gray.
     * @param ctx Context from which the RenderScript will run
     * @param bmp Processed bitmap image
     */
    public void toGrayRS(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Gray grayScript = new ScriptC_Gray(rs);
        grayScript.forEach_Gray(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }


    /**
     * Generate an image which only changes color for a certain range of hue between hue & chromakey.
     * @param ctx Context from which the RenderScript will run
     * @param bmp Processed bitmap image
     * @param hue initial hue value
     * @param tolerance closing hue value
     */
    public void keepColorRS(Context ctx, Bitmap bmp, float hue, float tolerance) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_KeepColor keepColorScript = new ScriptC_KeepColor(rs);
        keepColorScript.set_hue(hue);
        keepColorScript.set_tolerance(tolerance);
        keepColorScript.forEach_keepcolor(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        keepColorScript.destroy();
        rs.destroy();
    }

    /**
     * Function that changes the hue value of an image and adapts it to the entire Bitmap image.
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @param hue hue value to append
     */
    public void colorize(Context ctx, Bitmap bmp, float hue) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Colorize colorizeScript = new ScriptC_Colorize(rs);
        colorizeScript.set_rand(hue);
        colorizeScript.forEach_colorize(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();

    }

    /**
     * Generates an image with a different saturation
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @param saturation value of saturation to append
     */
    public void change_saturation(Context ctx, Bitmap bmp, float saturation) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Change_Saturation SaturationScript = new ScriptC_Change_Saturation(rs);
        SaturationScript.set_saturation_change(saturation);
        SaturationScript.forEach_change_saturation(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        SaturationScript.destroy();
        rs.destroy();

    }

    /**
     * Change brightness of the processed bitmap image
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @param brightness brightness value (V from HSV)
     */
    public void change_brightness(Context ctx, Bitmap bmp, float brightness) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Change_Brightness BrightnessScript = new ScriptC_Change_Brightness(rs);
        BrightnessScript.set_brightness_change(brightness);
        BrightnessScript.forEach_change_brightness(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        BrightnessScript.destroy();
        rs.destroy();

    }

    /**
     * Generates the negative of a bitmap image
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     */
    public void negative(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Negative NegativeScript = new ScriptC_Negative(rs);
        NegativeScript.forEach_negative(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        NegativeScript.destroy();
        rs.destroy();

    }

    /**
     * Generates and Returns histogram from bitmap image
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @return histogram
     */
    private int[] histogram(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        ScriptC_Histogram HistogramScript = new ScriptC_Histogram(rs);
        int[] histo = HistogramScript.reduce_histogram(input).get();

        input.destroy();
        HistogramScript.destroy();
        rs.destroy();
        return Tools.cumulativeHistogram(histo);
    }


    /**
     * Generates an image by equalizing histogram from bitmap image
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     */
    public void HistogramEqualizer(Context ctx, Bitmap bmp) {
        int[] histo = histogram(ctx, bmp);
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Histogram_Egalisation HEScript = new ScriptC_Histogram_Egalisation(rs);
        HEScript.set_cumulative_histogramm(histo);
        HEScript.set_tab_length(bmp.getWidth() * bmp.getHeight());
        HEScript.forEach_HistogramEqualize(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        HEScript.destroy();
        rs.destroy();

    }

    /**
     * Retrieves the max and min values from the bitmap image gray/color level values
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @return the min and max values (which is a 2 slot int[])
     */
    private int[] max_min(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        ScriptC_Max_Min max_minScript = new ScriptC_Max_Min(rs);
        int[] maxi_mini = max_minScript.reduce_MaxMin(input).get();
        input.destroy();
        max_minScript.destroy();
        rs.destroy();
        return maxi_mini;
    }

    /**
     * Extends the min and max values for a linear extension from the bitmap image
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     */
    public void LinearExtention(Context ctx, Bitmap bmp) {
        int[] tab = max_min(ctx, bmp);
        int[] LUTred = Contrast.createLUTred(new int[]{tab[0], tab[1]});
        int[] LUTgreen = Contrast.createLUTgreen(new int[]{tab[2], tab[3]});
        int[] LUTblue = Contrast.createLUTblue(new int[]{tab[4], tab[5]});

        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Linear_Extention LeScript = new ScriptC_Linear_Extention(rs);
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

    /**
     * Function that sets the RGB values with the coefficients
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @param red_coef red value coefficient
     * @param green_coef green value coefficient
     * @param blue_coef blue value coefficient
     */
    public void setRgbRS(Context ctx, Bitmap bmp, float red_coef, float green_coef, float blue_coef) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_SetRGB setRgbScript = new ScriptC_SetRGB(rs);
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

    /**
     * Apply Convolution filter in core field to bitmap image by using div.
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @param core filter to pass
     * @param core_length length of filter to pass
     * @param div division to apply
     */
    public void apply_filter(Context ctx, Bitmap bmp, float[] core, int core_length, int div) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        Allocation core_tab = Allocation.createSized(rs, Element.F32(rs), core_length);
        core_tab.copyFrom(core);
        ScriptC_Apply_Filter apf_script = new ScriptC_Apply_Filter(rs);
        apf_script.set_width(bmp.getWidth());
        apf_script.set_height(bmp.getHeight());
        apf_script.set_core_length(core_length);
        apf_script.bind_core(core_tab);

        apf_script.set_div(div);
        apf_script.set_gIn(input);
        apf_script.forEach_apply_filter(input, output);
        output.copyTo(bmp);
        output.destroy();
        core_tab.destroy();
        input.destroy();
        apf_script.destroy();
        rs.destroy();

    }

    /**
     * Mix two bitmap images into one.
     * @param ctx Context from which the RenderScript will run
     * @param bmp The result bitmap image
     * @param bmp1 The first bitmap image to mix
     * @param bmp2 The second bitmap image to mix
     */
    public void mix_bmp(Context ctx, Bitmap bmp, Bitmap bmp1, Bitmap bmp2) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation A1 = Allocation.createFromBitmap(rs, bmp1);
        Allocation A2 = Allocation.createFromBitmap(rs, bmp2);
        Allocation output = Allocation.createTyped(rs, A1.getType());
        ScriptC_MixBmp Mbmp_Script = new ScriptC_MixBmp(rs);

        Mbmp_Script.set_A1(A1);
        Mbmp_Script.set_A2(A2);
        Mbmp_Script.forEach_mix_bmp(input, output);
        output.copyTo(bmp);
        A1.destroy();
        A2.destroy();
        input.destroy();
        output.destroy();
        Mbmp_Script.destroy();
        rs.destroy();


    }

    /**
     * Horizontally reverse the bitmap
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     */
    public void reverseHor(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_ReverseHor RhScript = new ScriptC_ReverseHor(rs);
        RhScript.set_gIn(input);
        RhScript.set_height(bmp.getHeight());
        RhScript.forEach_reverseHor(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        RhScript.destroy();
        rs.destroy();
    }


    /**
     * Veritcally reverse the bitmap
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     */
    public void reverseVer(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_ReverseVer RvScript = new ScriptC_ReverseVer(rs);
        RvScript.set_gIn(input);
        RvScript.set_width(bmp.getWidth());
        RvScript.forEach_reverseHor(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        RvScript.destroy();
        rs.destroy();
    }

    /**
     * Rotate the bitmap image to the right by 90°
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @return
     */
    public Bitmap rotateRight(Context ctx, Bitmap bmp) {
        Bitmap newBmp = Bitmap.createBitmap(bmp.getHeight(), bmp.getWidth(), bmp.getConfig());
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createFromBitmap(rs,newBmp);
        ScriptC_RotateRight RRScript = new ScriptC_RotateRight(rs);
        RRScript.set_gOut(output);
        RRScript.set_width(bmp.getWidth());
        RRScript.set_height(bmp.getHeight());
        RRScript.forEach_rotateRight(input);
        output.copyTo(newBmp);
        input.destroy();
        output.destroy();
        RRScript.destroy();
        rs.destroy();
        return newBmp;
    }

    /**
     * Rotate the bitmap image to the left by 90°
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     * @return
     */
    public Bitmap rotateLeft(Context ctx, Bitmap bmp) {
        Bitmap newBmp = Bitmap.createBitmap(bmp.getHeight(), bmp.getWidth(), bmp.getConfig());
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createFromBitmap(rs,newBmp);
        ScriptC_RotateLeft RLScript = new ScriptC_RotateLeft(rs);
        RLScript.set_gOut(output);
        RLScript.set_width(bmp.getWidth());
        RLScript.set_height(bmp.getHeight());
        RLScript.forEach_rotateLeft(input);
        output.copyTo(newBmp);
        input.destroy();
        output.destroy();
        RLScript.destroy();
        rs.destroy();
        return newBmp;
    }

    /**
     * Function that creates an image of sepia colors.
     * @param ctx Context from which the RenderScript will run
     * @param bmp processed bitmap image
     */
    public void toSepiaRS(Context ctx, Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_Sepia sepiaScript = new ScriptC_Sepia(rs);
        sepiaScript.forEach_Sepia(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        sepiaScript.destroy();
        rs.destroy();
    }

}


