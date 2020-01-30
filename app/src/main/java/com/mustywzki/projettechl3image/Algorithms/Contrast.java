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

    protected Bitmap histogramEqualizationRGB(Bitmap bmp){ // Gray version
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        // Initialisation histogramC
        int[][] histogram = createHistogramRGB(pixels);
        int[] histogramC_red = new int [256];
        int[] histogramC_green = new int [256];
        int[] histogramC_blue = new int [256];
        histogramC_red[0] = histogram[0][0];
        histogramC_green[0] = histogram[1][0];
        histogramC_blue[0] = histogram[2][0];

        for (int i = 1; i < 256; i++){
            histogramC_red[i] = histogramC_red[i-1] + histogram[0][i];
            histogramC_green[i] = histogramC_green[i-1] + histogram[1][i];
            histogramC_blue[i] = histogramC_blue[i-1] + histogram[2][i];
        }

        // Initialisation LUT
        int[] LUT_red = new int [pixels.length];
        int[] LUT_green = new int [pixels.length];
        int[] LUT_blue = new int [pixels.length];

        for (int i = 0; i < pixels.length; i++){
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            int blue = Color.blue(pixels[i]);
            LUT_red[i] = 255 * histogramC_red[red] / pixels.length;
            LUT_green[i] = 255 * histogramC_green[green] / pixels.length;
            LUT_blue[i] = 255 * histogramC_blue[blue] / pixels.length;
        }

        for (int i = 0; i < pixels.length; i++){
            colors[i] = Color.rgb(LUT_red[i], LUT_green[i], LUT_blue[i]);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
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

    protected int[][] createHistogramRGB (int[] pixels){ // RGB version
        int red, green, blue;

        int[][] histogram = new int[3][256]; // histogram = {histogramRed[256], histogramGreen[256], histogramBlue[256]}

        for (int i = 0; i < pixels.length; i++){
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            histogram[0][red]++;
            histogram[1][green]++;
            histogram[2][blue]++;
        }

        return histogram;
    }
}
