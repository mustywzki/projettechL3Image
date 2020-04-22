package com.mustywzki.projettechl3image.algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Convolution {
    private static final double[] coreGaussian = {1.0, 2.0, 3.0, 2.0, 1.0
            ,2.0, 6.0, 8.0, 6.0, 2.0
            ,3.0, 8.0, 10.0, 8.0, 3.0
            ,2.0, 6.0, 8.0, 6.0, 2.0
            ,1.0, 2.0, 3.0, 2.0, 1.0};
    private static final double[] corePrewittHor = {-1, -1, -1
            ,0, 0, 0
            ,1, 1, 1};
    private static final double[] corePrewittVer = {-1, 0, 1
            ,-1, 0, 1
            ,-1, 0, 1};
    private static final double[] coreSobelVer = {-1, 0, 1
            ,-2, 0, 2
            ,-1, 0, 1};
    private static final double[] coreSobelHor = {-1, -2, -1
            ,0, 0, 0
            ,1, 2, 1};
    private static final double[] coreLap4 = {0, 1, 0
            ,1, -4, 1
            ,0, 1, 0};
    private static final double[] coreLap8 = {1, 1, 1
            ,1, -8, 1
            ,1, 1, 1};

    /**
     * Apply filter in core to bitmap image by using div.
     * @param bmp bitmap image
     * @param core filter to pass
     * @param div division to apply
     */
    private static void applyfilter(Bitmap bmp, double[] core, double div){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];

        double line = Math.sqrt(core.length);

        int dif = (int)(line - 1) / 2;

        double newRed, newGreen, newBlue;
        int id_color_to_set, id_color, id_core;

        for (int y = dif; y < (bmp.getHeight() - dif); y++){
            for (int x = dif; x < (bmp.getWidth() - dif); x++){

                id_color_to_set = y * bmp.getWidth() + x;
                newRed = 0;
                newGreen = 0;
                newBlue = 0;

                for (int j = 0; j < line; j++){
                    for (int i = 0; i < line; i++){
                        id_color = id_color_to_set + (j - dif) * bmp.getWidth() + (i - dif);
                        id_core = (int) (j * line + i);

                        newRed += Color.red(pixels[id_color]) * core[id_core];

                        newGreen += Color.green(pixels[id_color]) * core[id_core];

                        newBlue += Color.blue(pixels[id_color]) * core[id_core];
                    }
                }
                colors[id_color_to_set] = Color.rgb((int) Math.abs(newRed / div), (int) Math.abs(newGreen / div), (int) Math.abs(newBlue / div));
            }
        }

        bmp.setPixels(colors,0, bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    /**
     *  Mix two bitmap images into one.
     * @param bmp The result bitmap image
     * @param bmp1 The first bitmap image to mix
     * @param bmp2 The second bitmap image to mix
     */
    private static void mix_bmp(Bitmap bmp, Bitmap bmp1, Bitmap bmp2){
        int[] pixels_1 = new int[bmp.getWidth() * bmp.getHeight()];
        bmp1.getPixels(pixels_1, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] pixels_2 = new int[bmp.getWidth() * bmp.getHeight()];
        bmp2.getPixels(pixels_2, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        for (int i = 0; i < pixels_1.length; i++) {
            int red1 = Color.red(pixels_1[i]);
            int red2 = Color.red(pixels_2[i]);
            int green1 = Color.green(pixels_1[i]);
            int green2 = Color.green(pixels_2[i]);
            int blue1 = Color.blue(pixels_1[i]);
            int blue2 = Color.blue(pixels_2[i]);
            colors[i] = Color.rgb((red1 + red2)/2, (green1 + green2)/2, (blue1+blue2)/2);
        }
        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    /**
     * Call apply_filter on bmp with specified size for Average filter
     * @param bmp Processed bitmap image
     * @param size size of the average filter
     */
    public static void filter_Moyenneur(Bitmap bmp, int size){
        double[] core = new double[size];
        for (int i = 0; i < core.length; i++){
            core[i] = 1.0;
        }
        applyfilter(bmp, core, size);
    }

    /**
     * Call apply_filter on bmp with Gaussian filter
     * @param bmp Processed bitmap image
     */
    public static void filter_Gaussien(Bitmap bmp){
        applyfilter(bmp, coreGaussian, 98);
    }

    /* --- Prewitt --- */

    /**
     * Call apply_filter on bmp with Prewitt Filter
     * @param bmp Processed bitmap image
     */
    public static void filter_Prewitt(Bitmap bmp){
        Bitmap p_modif_ver = bmp;
        p_modif_ver = p_modif_ver.copy(p_modif_ver.getConfig(), true);

        Bitmap p_modif_hor = bmp;
        p_modif_hor = p_modif_hor.copy(p_modif_hor.getConfig(), true);

        filter_Prewitt_horizontal(p_modif_hor);
        filter_Prewitt_vertical(p_modif_ver);
        mix_bmp(bmp, p_modif_hor, p_modif_ver);
    }

    /**
     * Call apply_filter on bmp with Prewitt vertical filter
     * @param bmp Processed bitmap image
     */
    public static void filter_Prewitt_vertical(Bitmap bmp){
        applyfilter(bmp, corePrewittVer, 1);
    }

    /**
     * Call apply_filter on bmp with Prewitt horizontal filter
     * @param bmp Processed bitmap image
     */
    public static void filter_Prewitt_horizontal(Bitmap bmp){
        applyfilter(bmp, corePrewittHor, 1);
    }

    /* --- Sobel --- */
    /**
     * Call apply_filter on bmp with Sobel filter
     * @param bmp Processed bitmap image
     */
    public static void filter_Sobel(Bitmap bmp){
        Bitmap p_modif_ver = bmp;
        p_modif_ver = p_modif_ver.copy(p_modif_ver.getConfig(), true);

        Bitmap p_modif_hor = bmp;
        p_modif_hor = p_modif_hor.copy(p_modif_hor.getConfig(), true);

        filter_Sobel_horizontal(p_modif_hor);
        filter_Sobel_vertical(p_modif_ver);
        mix_bmp(bmp, p_modif_hor, p_modif_ver);
    }

    /**
     * Call apply_filter on bmp with Sobel vertical filter
     * @param bmp Processed bitmap image
     */
    public static void filter_Sobel_vertical(Bitmap bmp){
        applyfilter(bmp, coreSobelVer, 1);
    }
    /**
     * Call apply_filter on bmp with Sobel horizontal filter
     * @param bmp Processed bitmap image
     */
    public static void filter_Sobel_horizontal(Bitmap bmp){
        applyfilter(bmp, coreSobelHor, 1);
    }

    /* --- Laplacien --- */

    /**
     * Call apply_filter on bmp with Laplacier filtered with 4
     * @param bmp Processed bitmap image
     */
    public static void filter_Laplacier_4(Bitmap bmp){
        applyfilter(bmp, coreLap4, 1);
    }

    /**
     * Call apply_filter on bmp with Laplacier filtered with 8
     * @param bmp Processed bitmap image
     */
    public static void filter_Laplacier_8(Bitmap bmp){
        applyfilter(bmp, coreLap8, 1);
    }

}
