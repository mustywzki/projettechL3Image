package com.mustywzki.projettechl3image.Algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Test {

    private Functions functions;
    private Tools tools;

    public Test(Functions functions, Tools tools){
        this.functions = functions;
        this.tools = tools;
    }

    protected Bitmap testMyRGBToHSV(Bitmap bmp){ // Test if the picture after a chagement RGB -> HSV -> RGB is the same
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

            float[] hsv = tools.RGBToHSV(red, green, blue);
            colors[i] = tools.HSVToRGB(hsv);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap testThisRGBToHSV(Bitmap bmp){ // Test if the picture after a chagement RGB -> HSV -> RGB is the same
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

            float[] hsv = new float[3];

            Color.RGBToHSV(red, green, blue, hsv);
            colors[i] = Color.HSVToColor(hsv);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }
}
