#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"



float hue, tolerance;

uchar4 RS_KERNEL keepcolor(uchar4 in){
    float4 hsv = RgbToHsv(in);
    float h = fmod(hue,360);
    float t = fmod(tolerance,180);
    float diff = fabs(hsv[0] - hue);
    if (!(min(diff, 360 - diff) <= t)) {
        hsv[1] = 0;
    }


    const uchar4 output =  HsvToRgb(hsv);
    return output;
}


