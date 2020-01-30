package com.mustywzki.projettechl3image.Algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Functions {

    private Tools tools;
    private int left_selected_color;
    private int right_selected_color;

    /* --- Getter - Setter --- */

    public void setLeft_selected_color(int left_selected_color) {
        this.left_selected_color = 0;
    }

    public void setRight_selected_color(int right_selected_color) {
        this.right_selected_color = 360;
    }

    /* --- Method --- */

    public Functions(Tools tools){
        this.tools = tools;
    }

    protected Bitmap toGray(Bitmap bmp){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);
        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        for(int i = 0; i < pixels.length; i++) {
            colors[i] = tools.colorToGray(pixels[i]);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap colorize(Bitmap bmp){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);
        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        int red, green, blue;
        int new_color = (int) (Math.random() * 360);

        for(int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = tools.RGBToHSV(red, green, blue);
            hsv[0] = new_color;

            colors[i] = tools.HSVToRGB(hsv);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    // TODO Manage colors around 0 and around 360
    protected Bitmap selected_color(Bitmap bmp, int change_left, int change_right) {
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth() * p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth() * p_modif.getHeight()];

        int red, green, blue;
        left_selected_color = left_selected_color + change_left;
        if (left_selected_color < 0)
            left_selected_color = 0;
        if (left_selected_color > 360)
            left_selected_color = 360;
        right_selected_color = right_selected_color + change_right;
        if (right_selected_color < 0)
            right_selected_color = 0;
        if (right_selected_color > 360)
            right_selected_color = 360;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = tools.RGBToHSV(red, green, blue);
            if (!tools.isInside(hsv[0], left_selected_color, right_selected_color)){ //min : 0 ; max : 360
                colors[i] = tools.colorToGray(pixels[i]);
            }
            else{
                colors[i] = tools.HSVToRGB(hsv);
            }

        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap change_saturation(Bitmap bmp, double saturation_change){
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
            hsv[1] += saturation_change;
            if (hsv[1] < 0){
                hsv[1] = 0;
            }
            if (hsv[1] > 1){
                hsv[1] = 1;
            }
            colors[i] = tools.HSVToRGB(hsv);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap change_brightness (Bitmap bmp, double brightness_change){
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
            hsv[2] += brightness_change;
            if (hsv[2] < 0){
                hsv[2] = 0;
            }
            if (hsv[2] > 1){
                hsv[2] = 1;
            }
            colors[i] = tools.HSVToRGB(hsv);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap negative(Bitmap bmp){
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
