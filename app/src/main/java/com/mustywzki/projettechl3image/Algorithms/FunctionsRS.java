package com.mustywzki.projettechl3image.Algorithms;

import android.app.Activity;
import android.graphics.Bitmap;

import android.content.Context;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

public class FunctionsRS extends Activity {

    public void toGrayRS(Context ctx, Bitmap bmp, double redCoef, double greenCoef, double blueCoef){
        RenderScript rs = RenderScript.create(ctx) ;
        // Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs, bmp);
        Allocation output = Allocation.createTyped (rs, input.getType()) ;
        // Creer le script
        ScriptC_gray grayScript = new ScriptC_gray(rs);
        // Copier les donnees dans les Allocations
        // ici inutile
        // Initialiser les variables globales potentielles
        grayScript.set_newr(redCoef);
        grayScript.set_newg(greenCoef);
        grayScript.set_newb(blueCoef);
        // Lancer le noyau
        grayScript.forEach_toGray(input, output);
        // Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
        // Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }

    public void keepColorRS(Context ctx, Bitmap bmp, float hue, float chromaKey) {
        RenderScript rs = RenderScript.create(ctx);
        Allocation input = Allocation.createFromBitmap(rs,bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_keepColor keepColorScript = new ScriptC_keepColor(rs);
        keepColorScript.set_hue(hue);
        keepColorScript.set_chromakey(chromaKey);
        keepColorScript.forEach_keepColor(input, output);
        output.copyTo(bmp);
        input.destroy();
        output.destroy();
        keepColorScript.destroy();
        rs.destroy();
    }
}
