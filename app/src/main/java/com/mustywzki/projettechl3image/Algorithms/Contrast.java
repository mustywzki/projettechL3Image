package com.mustywzki.projettechl3image.Algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Contrast {

    // TODO contraste with HSV and linear extension less
    public static void linear_transformation(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];
        int red, green, blue;

        int[] LUTred = createLUTred(Tools.max_min_red(pixels));
        int[] LUTgreen = createLUTgreen(Tools.max_min_green(pixels));
        int[] LUTblue = createLUTblue(Tools.max_min_blue(pixels));

        for (int i = 0; i < pixels.length; i++){
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            red = LUTred[red];
            green = LUTgreen[green];
            blue = LUTblue[blue];

            colors[i] = Color.rgb(red, green, blue);
        }

        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
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

    protected static int[] createLUTred (int[] max_min_red){
        int max_red = max_min_red[0];
        int min_red = max_min_red[1];
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

    protected static int[] createLUTgreen (int[] max_min_green){
        int max_green = max_min_green[0];
        int min_green = max_min_green[1];
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

    protected static int[] createLUTblue (int[] max_min_blue) {
        int max_blue = max_min_blue[0];
        int min_blue = max_min_blue[1];
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
