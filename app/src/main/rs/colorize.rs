#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"


float rand;

uchar4 RS_KERNEL Colorize(uchar4 in){
    float4 hsv = RgbToHsv(in);
    hsv[0] = rand;
    uchar4 rgb= HsvToRgb(hsv);
    return rgb;
}



