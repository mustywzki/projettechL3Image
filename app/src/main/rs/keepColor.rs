#pragma version(1)
#pragma rs java_package_name(com.android.rssample)
#define MODULO(x,y) (x-  ( ( (int)(x/y) ) * y)  )

float hue, chromakey;

uchar4 RS_KERNEL keepColor(uchar4 in){


    hue = MODULO(hue,360);
    chromakey = MODULO(chromakey,180);
    uchar4 out;
    float h,s,v;
    float R=(float)((float)(in.r)/255.0);
    float G=(float)((float)(in.g)/255.0);
    float B=(float)((float)(in.b)/255.0);
    float Cmax = (float)max(max(R,G), B);
    float Cmin = (float)min(min(R,G), B);
    float delta = (float)(Cmax-Cmin);
    h=-1.0;

    if(delta==0){
        h=0.0;
    } else if(Cmax==R){
    h = (float) ( 60.0* ( (int) ( MODULO( ((G - B)/ delta),6)) ) );
    } else if(Cmax==G){
    h = (float)(60.0 * (((B - R) / delta) + 2.0));
    } else if(Cmax==B){
    h=(float)(60.0*(((R-G)/delta)+1.0));
    }
    float diff = fabs(h - hue);

    if (!(fmin(diff, 360 - diff) <= chromakey)) {
        out=in;
        out.r=(in.r+in.g+in.b)/3;
        out.g=(in.r+in.g+in.b)/3;
        out.b=(in.r+in.g+in.b)/3;
    }

   /*  if(h>30 && h<330){
    out=in;
    out.r=(in.r+in.g+in.b)/3;
    out.g=(in.r+in.g+in.b)/3;
    out.b=(in.r+in.g+in.b)/3;
    return out;
     }

     return in;*/
     return out;
}

/*
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
   */