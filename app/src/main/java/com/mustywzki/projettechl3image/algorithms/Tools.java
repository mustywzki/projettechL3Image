package com.mustywzki.projettechl3image.algorithms;


import android.graphics.Bitmap;
import android.graphics.Color;

public class Tools {

    /**
     * Convert RGB values to HSV
     * @param red red channel
     * @param green green channel
     * @param blue blue channel
     * @return HSV values
     */
    static float[] RGBToHSV(int red, int green, int blue){
        float[] hsv = new float[3];

        float r = red / 255.F;
        float g = green / 255.F;
        float b = blue / 255.F;

        float Cmax = Math.max(r, Math.max(g, b));
        float Cmin = Math.min(r, Math.min(g, b));
        float delta = Cmax - Cmin;

        /* --- Hue calculation --- */
        if (delta == 0){
            hsv[0] = 0;
        }
        else if (Cmax == r){
            hsv[0] = ((g - b)/delta)%6;
        }
        else if (Cmax == g){
            hsv[0] = (b - r)/delta + 2;
        }
        else if (Cmax == b){
            hsv[0] = (r - g)/delta + 4;
        }
        hsv[0] = hsv[0] * 60;

        if (hsv[0] < 0){
            hsv[0] += 360;
        }

        /* --- Saturation calculation --- */
        if (Cmax == 0){
            hsv[1] = 0;
        }
        else{
            hsv[1] = delta / Cmax;
        }

        /* --- Value calculation --- */
        hsv[2] = Cmax;

        return hsv;
    }

    /**
     * Convert HSV values to RGB
     * @param hsv hsv float containing H,S,V values
     * @param alpha predefined alpha to set on the RGBa.
     * @return int of a RGB value.
     */
    static int HSVToRGB(float[] hsv, int alpha){
        float h = hsv[0];
        float s = hsv[1];
        float v = hsv[2];

        float c = v * s;
        float x = c * (1 - Math.abs(((h/60) % 2) - 1));
        float m = v - c;

        float r = 0;
        float g = 0;
        float b = 0;

        if (h >= 0){
            if (h < 60){
                r = c;
                g = x;
                b = 0;
            }
            else if (h < 120){
                r = x;
                g = c;
                b = 0;
            }
            else if (h < 180){
                r = 0;
                g = c;
                b = x;
            }
            else if (h < 240){
                r = 0;
                g = x;
                b = c;
            }
            else if (h < 300){
                r = x;
                g = 0;
                b = c;
            }
            else if (h < 360){
                r = c;
                g = 0;
                b = x;
            }
        }

        int red = (int) ((r + m) * 255);
        int green = (int) ((g + m) * 255);
        int blue = (int) ((b + m) * 255);

        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Generates and Returns Histogram from bitmap image
     * @param bmp Bitmap Image
     * @return Histogram
     */
    public static int[] getHistogram(Bitmap bmp) {
        int[] hist = new int[256];
        int[] tmpCopy = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int i = 0; i < tmpCopy.length; i++) {
            int px = tmpCopy[i];
            float[] hsv = Tools.RGBToHSV(Color.red(px), Color.green(px), Color.blue(px));
            hist[(int) (hsv[2] * 255f)]++;
        }
        return hist;
    }

    /**
     * Creates a cumulative Histogram from a base histogram
     * @param hist Histogram
     * @return Cumulative Histogram
     */
    public static int[] cumulativeHistogram(int[] hist){
        int[] C = new int[256];
        C[0] = hist[0];
        for (int k = 1; k < hist.length; k++){
            C[k] = hist[k] + C[k-1];
        }
        return C;
    }
}