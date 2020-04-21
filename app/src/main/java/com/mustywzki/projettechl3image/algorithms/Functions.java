package com.mustywzki.projettechl3image.algorithms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

public class Functions {

    /**
     * Function that creates an image of shades of gray
     * @param bmp Processed bitmap image
     */
    public static void toGray(Bitmap bmp){
        int[] pixels = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0,0,bmp.getWidth(),bmp.getHeight());

        for(int i = 0; i < pixels.length; i++) {
            int currentPixel = pixels[i];
            double grayValue = 0.3 * Color.red(currentPixel);
            grayValue += 0.59 * Color.blue(currentPixel);
            grayValue += 0.11 * Color.green(currentPixel);
            pixels[i] = Color.rgb((int)grayValue,(int)grayValue,(int)grayValue);
        }
        bmp.setPixels(pixels,0, bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    public static void toSepia(Bitmap bmp){
        int[] pixels = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0,0,bmp.getWidth(),bmp.getHeight());

        double red, green, blue;

        for(int i = 0; i < pixels.length; i++) {
            int currentPixel = pixels[i];
            double old_red = Color.red(currentPixel);
            double old_green = Color.green(currentPixel);
            double old_blue = Color.blue(currentPixel);
            red = old_red*0.393 + old_green*0.769 + old_blue*0.189;
            green = old_red*0.349 + old_green*0.686 + old_blue*0.168;
            blue = old_red*0.272 + old_green*0.534 + old_blue*0.131;
            pixels[i] = Color.rgb((int)red,(int)green,(int)blue);
        }
        bmp.setPixels(pixels,0, bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    /**
     * Function that changes the hue value of an image and adapts it to the entire Bitmap image.
     * @param bmp processed bitmap image
     * @param hue hue value to append
     */
    public static void colorize(Bitmap bmp, float hue) {
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            int currentPixel = pixels[i];
            float[] hsv = Tools.RGBToHSV(Color.red(currentPixel), Color.green(currentPixel), Color.blue(currentPixel)); // Getting HSV values for the currentPixel
            hsv[0] = hue; // setting up the new hue selected
            pixels[i] = Tools.HSVToRGB(hsv, Color.alpha(currentPixel)); // Setting the HSV values back to RGB in order to set the modified pixel
        }
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    /**
     * Generate an image which only changes color for a certain range of hue between hue & chromakey.
     * @param bmp processed bitmap image
     * @param hue initial hue value
     * @param chromakey closing hue value
     */
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

    /**
     * Generates an image with a different saturation
     * @param bmp processed bitmap image
     * @param saturation_change value of saturation to append
     */
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

    /**
     * Change brightness of the processed bitmap image
     * @param bmp processed bitmap image
     * @param brightness_change brightness value (V from HSV)
     */
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

    /**
     * Generates the negative of a bitmap image
     * @param bmp processed bitmap image
     */
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

    /**
     * Function that set the RGB values with the coefs
     * @param bmp processed bitmap image
     * @param red_coef the coef for red value
     * @param green_coef the coef for green value
     * @param blue_coef the coef for blue value
     */
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

    /**
     * Rotate the bmp to the left with 90°
     * @param bmp processed bitmap image
     * @return the new Bitmap
     */
    public static Bitmap rotateLeft(Bitmap bmp){
        Bitmap newBmp = Bitmap.createBitmap(bmp.getHeight(), bmp.getWidth(), bmp.getConfig());

        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] new_pixels = new int[newBmp.getWidth() * newBmp.getHeight()];

        int ind_pixels = 0;
        for (int x = 0; x < newBmp.getWidth(); x++){
            for (int y = newBmp.getHeight() -1; y >= 0; y--){
                int ind_pixels_new = y * newBmp.getWidth() + x;
                new_pixels[ind_pixels_new] = pixels[ind_pixels];
                ind_pixels++;
            }
        }
        newBmp.setPixels(new_pixels,0, newBmp.getWidth(),0,0,newBmp.getWidth(),newBmp.getHeight());
        return newBmp;
    }

    /**
     * Rotate the bmp to the right with 90°
     * @param bmp processed bitmap image
     * @return the new Bitmap
     */
    public static Bitmap rotateRight(Bitmap bmp){
        Bitmap newBmp = Bitmap.createBitmap(bmp.getHeight(), bmp.getWidth(), bmp.getConfig());

        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] new_pixels = new int[newBmp.getWidth() * newBmp.getHeight()];

        int ind_pixels = 0;
        for (int x = newBmp.getWidth() - 1; x >= 0; x--){
            for (int y = 0; y < newBmp.getHeight(); y++){
                int ind_pixels_new = y * newBmp.getWidth() + x;
                new_pixels[ind_pixels_new] = pixels[ind_pixels];
                ind_pixels++;
            }
        }
        newBmp.setPixels(new_pixels,0, newBmp.getWidth(),0,0,newBmp.getWidth(),newBmp.getHeight());
        return newBmp;
    }

    /**
     * Vertically reverse the bitmap
     * @param bmp processed bitmap image
     */
    public static void reverseVer(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int y = 0; y < bmp.getHeight(); y++){
            int ind_right = bmp.getWidth() * (y +1) - 1;
            int ind_left = bmp.getWidth() * y;
            for (int x = 0; x < bmp.getWidth()/2; x++){
                int tmp = pixels[ind_left];
                pixels[ind_left] = pixels[ind_right];
                pixels[ind_right] = tmp;
                ind_left++;
                ind_right--;
            }
        }
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    /**
     * Horizontally reverse the bitmap
     * @param bmp processed bitmap image
     */
    public static void reverseHor(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int x = 0; x < bmp.getWidth(); x++){
            for (int y = 0; y < bmp.getHeight()/2; y++){
                int ind_bot = bmp.getWidth() * y + x;
                int ind_top = bmp.getWidth() * (bmp.getHeight()-y-1) + x;
                int tmp = pixels[ind_bot];
                pixels[ind_bot] = pixels[ind_top];
                pixels[ind_top] = tmp;
            }
        }
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }
}
