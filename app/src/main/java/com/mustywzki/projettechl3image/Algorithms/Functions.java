package com.mustywzki.projettechl3image.Algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Functions {

    /* --- Method --- */

    public static void toGray(Bitmap bmp){

        // Scaling RGB values to the specific range (equalizer)
        double red_coef = 0.3;
        double green_coef = 0.59;
        double blue_coef = 0.11;

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

    // TODO change in red color when add, maybe when > (not pb with HSV/RGB)
    public static void change_saturation(Bitmap bmp, float saturation_change){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = Tools.RGBToHSV(red, green, blue);
            hsv[1] += (saturation_change - 50)*0.02;
            if (hsv[1] < 0){
                hsv[1] = 0;
            }
            if (hsv[1] > 1){
                hsv[1] = 1;
            }
            colors[i] = Tools.HSVToRGB(hsv, Color.alpha(pixels[i]));
        }

        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public static void change_brightness (Bitmap bmp, float brightness_change){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = Tools.RGBToHSV(red, green, blue);
            hsv[2] += (brightness_change - 50)*0.02;
            if (hsv[2] < 0){
                hsv[2] = 0;
            }
            if (hsv[2] > 1){
                hsv[2] = 1;
            }
            colors[i] = Tools.HSVToRGB(hsv, Color.alpha(pixels[i]));
        }

        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public static void negative(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];
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

        bmp.setPixels(colors,0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public static void setRGB(Bitmap bmp, double red_coef, double green_coef, double blue_coef){

        red_coef = (red_coef - 50)*0.02;
        green_coef = (green_coef - 50)*0.02;
        blue_coef = (blue_coef - 50)*0.02;

        int[] pixels = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0,0,bmp.getWidth(),bmp.getHeight());

        // Applying gray filter
        for(int i = 0; i < pixels.length; i++) {
            double redValue = Color.red(pixels[i]);
            redValue += 255*red_coef;
            if (redValue < 0) redValue = 0;
            if (redValue > 255) redValue = 255;

            double blueValue = Color.blue(pixels[i]);
            blueValue += 255*blue_coef;
            if (blueValue < 0) blueValue = 0;
            if (blueValue > 255) blueValue = 255;

            double greenValue = Color.green(pixels[i]);
            greenValue += 255*green_coef;
            if (greenValue < 0) greenValue = 0;
            if (greenValue > 255) greenValue = 255;

            pixels[i] = Color.rgb((int)redValue, (int)greenValue, (int)blueValue);
        }
        bmp.setPixels(pixels,0, bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
}
