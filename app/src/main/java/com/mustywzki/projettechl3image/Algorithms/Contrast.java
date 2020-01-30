package com.mustywzki.projettechl3image.Algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Contrast {

    private Tools tools;
    private Functions functions;

    public Contrast (Tools tools, Functions functions){
        this.tools = tools;
        this.functions = functions;
    }

    // TODO contraste with HSV and linear extension less
    protected Bitmap linear_transformation(Bitmap bmp){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];
        int red, green, blue;

        int[] LUTred = createLUTred(tools.max_min_red(pixels));
        int[] LUTgreen = createLUTgreen(tools.max_min_green(pixels));
        int[] LUTblue = createLUTblue(tools.max_min_blue(pixels));

        for (int i = 0; i < pixels.length; i++){
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            red = LUTred[red];
            green = LUTgreen[green];
            blue = LUTblue[blue];

            colors[i] = Color.rgb(red, green, blue);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    public static void histogramEqualizer(int[] hist, Bitmap bmp){
        int[] cumulativeHist;
        cumulativeHist = Tools.cumulativeHistogram(hist);

        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int i = 0; i < bmp.getHeight()*bmp.getWidth(); i++){
            int px = pixels[i];
            float[] hsv = Tools.RGBToHSV(Color.red(px), Color.green(px), Color.blue(px));
            hsv[2] = (float) (cumulativeHist[(int) (hsv[2] * 255f)] * 255 / pixels.length) / 255f;
            pixels[i] = Tools.HSVToRGB(hsv, Color.alpha(px));
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    protected int[] createLUTred (int[] max_min_array){
        int max_red = max_min_array[0];
        int min_red = max_min_array[1];
        int[] LUTred = new int[256];

        for (int ng = 0; ng < 256; ng++){
            if (max_red != min_red || max_red - min_red == 0) {
                LUTred[ng] = (255 * (ng - min_red) / (max_red - min_red));
            }
            else {
                LUTred[ng] = max_red;
            }
        }
        return LUTred;
    }

    protected int[] createLUTgreen (int[] max_min_array){
        int max_green = max_min_array[2];
        int min_green = max_min_array[3];
        int[] LUTgreen = new int[256];

        for (int ng = 0; ng < 256; ng++){
            if (max_green != min_green || max_green - min_green == 0) {
                LUTgreen[ng] = (255 * (ng - min_green) / (max_green - min_green));
            }
            else {
                LUTgreen[ng] = max_green;
            }
        }
        return LUTgreen;
    }

    protected int[] createLUTblue (int[] max_min_array) {
        int max_blue = max_min_array[4];
        int min_blue = max_min_array[5];
        int[] LUTblue = new int[256];

        for (int ng = 0; ng < 256; ng++) {
            if (max_blue != min_blue || max_blue - min_blue == 0) {
                LUTblue[ng] = (255 * (ng - min_blue) / (max_blue - min_blue));
            } else {
                LUTblue[ng] = max_blue;
            }
        }
        return LUTblue;

    }
}
