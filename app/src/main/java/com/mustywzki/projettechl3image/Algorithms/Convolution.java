package com.mustywzki.projettechl3image.Algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Convolution {

    private Tools tools;
    private Functions functions;

    public Convolution(Tools tools, Functions functions){
        this.tools = tools;
        this.functions = functions;
    }

    // TODO doesn't create a gray picture (there is yellow) when negative value, not in 0-255
    private Bitmap applyfilter(Bitmap bmp, double[] core, int div){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];

        double line = Math.sqrt(core.length);
        if(line < 2){
            line = 0;
        }
        int dif = (int)(line - 1) / 2;

        double newRed, newGreen, newBlue;
        int id_color_to_set;

        for (int y = dif; y < (bmp.getHeight() - dif); y++){
            for (int x = dif; x < (bmp.getWidth() - dif); x++){

                id_color_to_set = y * bmp.getWidth() + x;
                newRed = 0;
                newGreen = 0;
                newBlue = 0;

                for (int j = 0; j < line; j++){
                    for (int i = 0; i < line; i++){
                        int id_color = id_color_to_set + (j - dif) * bmp.getWidth() + (i - dif);
                        int id_core = (int) (j * line + i);

                        newRed += Color.red(pixels[id_color]) * (core[id_core] / div);
                        newGreen += Color.green(pixels[id_color]) * (core[id_core] / div);
                        newBlue += Color.blue(pixels[id_color]) * (core[id_core] / div);
                    }
                }

                colors[id_color_to_set] = Color.rgb((int) newRed, (int) newGreen, (int) newBlue);
            }
        }

        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);
        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    public Bitmap filter_Moyenneur(Bitmap bmp, int size){
        double[] core = new double[size];
        for (int i = 0; i < core.length; i++){
            core[i] = 1.0;
        }
        return applyfilter(bmp, core, size);
    }

    public Bitmap filter_Gaussien(Bitmap bmp){
        double[] core = {1.0, 2.0, 3.0, 2.0, 1.0
                        ,2.0, 6.0, 8.0, 6.0, 2.0
                        ,3.0, 8.0, 10.0, 8.0, 3.0
                        ,2.0, 6.0, 8.0, 6.0, 2.0
                        ,1.0, 2.0, 3.0, 2.0, 1.0};
        return applyfilter(bmp, core, 98);
    }

    /* --- Prewitt --- */

    public Bitmap filter_Prewitt(Bitmap bmp){
        bmp = filter_Prewitt_horizontal(bmp);
        bmp = filter_Prewitt_vertical(bmp);
        return bmp;
    }

    public Bitmap filter_Prewitt_horizontal(Bitmap bmp){
        double[] core = {-1, 0, 1
                        ,-1, 0, 1
                        ,-1, 0, 1};
        return applyfilter(bmp, core, 1);
    }

    public Bitmap filter_Prewitt_vertical(Bitmap bmp){
        double[] core = {-1, -1, -1
                        ,0, 0, 0
                        ,1, 1, 1};
        return applyfilter(bmp, core, 1);
    }

    /* --- Sobel --- */

    public Bitmap filter_Sobel(Bitmap bmp){
        bmp = filter_Prewitt_horizontal(bmp);
        bmp = filter_Prewitt_vertical(bmp);
        return bmp;
    }

    public Bitmap filter_Sobel_horizontal(Bitmap bmp){
        double[] core = {-1, 0, 1
                        ,-2, 0, 2
                        ,-1, 0, 1};
        return applyfilter(bmp, core, 1);
    }

    public Bitmap filter_Sobel_vertical(Bitmap bmp){
        double[] core = {-1, -2, -1
                        ,0, 0, 0
                        ,1, 2, 1};
        return applyfilter(bmp, core, 1);
    }

    /* --- Laplacien --- */

    public Bitmap filter_Laplacier_4(Bitmap bmp){
        double[] core = {0, 1, 0
                        ,1, -4, 1
                        ,0, 1, 0};
        return applyfilter(bmp, core, 1);
    }

    public Bitmap filter_Laplacier_8(Bitmap bmp){
        double[] core = {1, 1, 1
                        ,1, -8, 1
                        ,1, 1, 1};
        return applyfilter(bmp, core, 1);
    }

}
