#pragma version(1)
#pragma rs java_package_name(com.android.rssample)
#define MODULO(x,y) (x-  ( ( (int)(x/y) ) * y)  )

uchar4 RS_KERNEL keepColor(uchar4 in){

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

     if(h>30 && h<330){
    out=in;
    out.r=(in.r+in.g+in.b)/3;
    out.g=(in.r+in.g+in.b)/3;
    out.b=(in.r+in.g+in.b)/3;
    return out;
     }

     return in;





}