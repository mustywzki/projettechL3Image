#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

uchar4  RS_KERNEL HsvToRgb(float4  hsv) {
    float h = hsv[0];
    float s = hsv[1];
    float v = hsv[2];
    float alpha = hsv[3];

    float c = v * s;
    float z= fmod((h/60),2);
    float x = c * (1 -  fabs(z-1));

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

    float m = v - c;

    float red, green, blue;

    red = (r + m);
    green = (g + m);
    blue = (b + m);
    float4 rgba = {red, green, blue, hsv[3]};
    return rsPackColorTo8888(rgba);

}