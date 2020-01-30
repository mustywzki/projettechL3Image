package com.mustywzki.projettechl3image.Algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Functions {

    /* --- Method --- */

    public static void toGray(Bitmap bmp, double red_coef, double green_coef, double blue_coef){

        // Scaling RGB values to the specific range (equalizer)
        red_coef = red_coef > 1.0 ? 1.0 : red_coef;
        red_coef = red_coef < 0.0 ? 0.0 : red_coef;

        blue_coef = blue_coef > 1.0 ? 1.0 : blue_coef;
        blue_coef = blue_coef < 0.0 ? 0.0 : blue_coef;

        green_coef = green_coef > 1.0 ? 1.0 : green_coef;
        green_coef = green_coef < 0.0 ? 0.0 : green_coef;

        // Copying the bitmap bmp's pixels into a int[] in order to perform the algorithm faster using getPixels()
        int[] tmpCopy = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(tmpCopy, 0, bmp.getWidth(), 0,0,bmp.getWidth(),bmp.getHeight());

        // Applying gray filter
        for(int i = 0; i < tmpCopy.length; i++) {
            int currentPixel = tmpCopy[i];
            double grayValue = red_coef * Color.red(currentPixel);
            grayValue += blue_coef * Color.blue(currentPixel);
            grayValue += green_coef * Color.green(currentPixel);
            tmpCopy[i] = Color.rgb((int)grayValue,(int)grayValue,(int)grayValue);
        }
        bmp.setPixels(tmpCopy,0, bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    public static void colorize(Bitmap bmp, float hue) {

        // Copying the bitmap bmp's pixels into a int[] in order to perform the algorithm faster using getPixels()
        int[] tmpCopy = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        // Applying hue modifications
        for (int i = 0; i < tmpCopy.length; i++) {
            int currentPixel = tmpCopy[i];
            float[] hsv = Tools.RGBToHSV(Color.red(currentPixel), Color.green(currentPixel), Color.blue(currentPixel)); // Getting HSV values for the currentPixel
            hsv[0] = hue; // setting up the new hue selected
            tmpCopy[i] = Tools.HSVToRGB(hsv, Color.alpha(currentPixel)); // Setting the HSV values back to RGB in order to set the modified pixel
        }
        bmp.setPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public static void keepColor(Bitmap bmp, float hue, float chromakey) {
        hue = hue % 360f;
        chromakey = chromakey % 180f;
        int[] tmpCopy = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        for (int i = 0; i < tmpCopy.length; i++) {
            int currentPixel = tmpCopy[i];
            float[] hsvValues = Tools.RGBToHSV(Color.red(currentPixel), Color.green(currentPixel), Color.blue(currentPixel));
            float diff = Math.abs(hsvValues[0] - hue);
            if (!(Math.min(diff, 360 - diff) <= chromakey)) {
                hsvValues[1] = 0;
            }
            tmpCopy[i] = Tools.HSVToRGB(hsvValues, Color.alpha(currentPixel));
        }
        bmp.setPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public static Bitmap change_saturation(Bitmap bmp, double saturation_change){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth() * p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth() * p_modif.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = Tools.RGBToHSV(red, green, blue);
            hsv[1] += saturation_change;
            if (hsv[1] < 0){
                hsv[1] = 0;
            }
            if (hsv[1] > 1){
                hsv[1] = 1;
            }
            colors[i] = Tools.HSVToRGB(hsv, Color.alpha(pixels[i]));
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    public static Bitmap change_brightness (Bitmap bmp, double brightness_change){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth() * p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth() * p_modif.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = Tools.RGBToHSV(red, green, blue);
            hsv[2] += brightness_change;
            if (hsv[2] < 0){
                hsv[2] = 0;
            }
            if (hsv[2] > 1){
                hsv[2] = 1;
            }
            colors[i] = Tools.HSVToRGB(hsv, Color.alpha(pixels[i]));
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    public static Bitmap negative(Bitmap bmp){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];
        int red, green, blue;

        for(int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            red = 255 - red;
            green = 255 - green;
            blue = 255 - blue;

            colors[i] = Color.rgb(red, green, blue);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }
}
