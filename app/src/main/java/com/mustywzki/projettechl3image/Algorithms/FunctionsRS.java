package com.mustywzki.projettechl3image.Algorithms;

import android.app.Activity;
import android.graphics.Bitmap;

import com.mustywzki.imageandroidproj.ScriptC_colorize;
import com.mustywzki.imageandroidproj.ScriptC_gray;
import com.mustywzki.imageandroidproj.ScriptC_keepColor;
import com.mustywzki.projettechl3image.MainActivity;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

public class FunctionsRS extends Activity {


    public FunctionsRS(){
    }

    public void toGrayRS(Context ctx, Bitmap bmp, double red_coef, double green_coef, double blue_coef){
        RenderScript rs = RenderScript.create(ctx) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs, bmp);
        Allocation output = Allocation.createTyped ( rs , input.getType()) ;
        // 3) Creer le script
        ScriptC_gray grayScript = new ScriptC_gray(rs);
        // 4) Copier les donnees dans les Allocations
        // ici inutile
        // 5) Initialiser les variables globales potentielles
        grayScript.set_newr(red_coef);
        grayScript.set_newg(green_coef);
        grayScript.set_newb(blue_coef);
        // 6) Lancer le noyau
        grayScript.forEach_toGray(input, output);
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }

    public void keepColorRS(Context ctx, Bitmap bmp, float hue, float chromakey) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_keepColor keepColorScript = new ScriptC_keepColor(rs);
        keepColorScript.set_hue(hue);
        keepColorScript.set_chromakey(chromakey);
        keepColorScript.forEach_keepColor(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        keepColorScript.destroy();
        rs.destroy();
    }

    public void colorizeRS(Context ctx, Bitmap bmp, float hue){
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs);
        colorizeScript.set_hue(hue);
        colorizeScript.forEach_colorize(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();
    }

}
