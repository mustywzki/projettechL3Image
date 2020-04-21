package com.mustywzki.projettechl3image.algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Contrast {

    private static int maxRed;
    private static int minRed;
    private static int maxGreen;
    private static int minGreen;
    private static int maxBlue;
    private static int minBlue;

    /**
     * Algorithm for linear transformation applied on a bitmap image
     * @param bmp processed bitmap image
     */
    public static void linear_transformation(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];
        int red, green, blue;

        max_min_red(pixels);
        max_min_green(pixels);
        max_min_blue(pixels);
        int[] LUTred = createLUTred();
        int[] LUTgreen = createLUTgreen();
        int[] LUTblue = createLUTblue();

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

    /**
     * Generates an image with an equalized histogram
     * @param hist histogram to equalize
     * @param bmp bitmap image to modify
     */
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

    /**
     * Generates red channel Look-Up Table
     * @return Red LUT
     */
    private static int[] createLUTred (){
        int[] LUTred = new int[256];

        for (int ng = 0; ng < 256; ng++){
            if (maxRed != minRed) {
                LUTred[ng] = (255 * (ng - minRed) / (maxRed - minRed));
            }
            else {
                LUTred[ng] = maxRed;
            }
        }
        return LUTred;
    }

    /**
     * Generates red channel Look-Up Table
     * @param max_min_red max & minimal ranges for red
     * @return Red LUT
     */
    public static int[] createLUTred (int[] max_min_red){
        maxRed = max_min_red[0];
        minRed = max_min_red[1];
        return createLUTred();
    }

    /**
     * Generates green channel Look-Up Table
     * @return Green LUT
     */
    private static int[] createLUTgreen (){
        int[] LUTgreen = new int[256];

        for (int ng = 0; ng < 256; ng++){
            if (maxGreen != minGreen) {
                LUTgreen[ng] = (255 * (ng - minGreen) / (maxGreen - minGreen));
            }
            else {
                LUTgreen[ng] = maxGreen;
            }
        }
        return LUTgreen;
    }

    /**
     * Generates green channel Look-Up Table
     * @param max_min_green max & minimal ranges for green
     * @return Green LUT
     */
    public static int[] createLUTgreen (int[] max_min_green){
        maxGreen = max_min_green[0];
        minGreen = max_min_green[1];
        return createLUTgreen();
    }

    /**
     * Generates blue channel Look-Up Table
     * @return Blue LUT
     */
    private static int[] createLUTblue () {
        int[] LUTblue = new int[256];

        for (int ng = 0; ng < 256; ng++) {
            if (maxBlue != minBlue) {
                LUTblue[ng] = (255 * (ng - minBlue) / (maxBlue - minBlue));
            } else {
                LUTblue[ng] = maxBlue;
            }
        }
        return LUTblue;

    }

    /**
     * Generates blue channel Look-Up Table
     * @param max_min_blue max & minimal ranges for blue
     * @return Blue LUT
     */
    public static int[] createLUTblue (int[] max_min_blue){
        maxBlue = max_min_blue[0];
        minBlue = max_min_blue[1];
        return createLUTblue();
    }

    /**
     * Finds red min/max range
     * @param pixels pixels of the image
     */
    private static void max_min_red (int[] pixels){
        int max_red = 0, min_red = 255, red;

        for (int i = 0; i < pixels.length; i++){
            red = Color.red(pixels[i]);

            if (red > max_red)
                max_red = red;
            if (red < min_red)
                min_red = red;
        }
        maxRed = max_red;
        minRed = min_red;
    }

    /**
     * Finds green min/max range
     * @param pixels pixels of the image
     */
    private static void max_min_green (int[] pixels){
        int max_green = 0, min_green = 255, green;

        for (int i = 0; i < pixels.length; i++){
            green = Color.green(pixels[i]);

            if (green > max_green)
                max_green = green;
            if (green < min_green)
                min_green = green;
        }
        minGreen = min_green;
        maxGreen = max_green;
    }

    /**
     * Finds blue min/max range
     * @param pixels pixels of the image
     */
    private static void max_min_blue (int[] pixels){
        int max_blue = 0, min_blue = 255, blue;

        for (int i = 0; i < pixels.length; i++){
            blue = Color.blue(pixels[i]);

            if (blue > max_blue)
                max_blue = blue;
            if (blue < min_blue)
                min_blue = blue;
        }
        minBlue = min_blue;
        maxBlue = max_blue;
    }
}
