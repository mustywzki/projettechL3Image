#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"

float saturation_change;

uchar4 RS_KERNEL change_saturation(uchar4 in){
    float4 hsv = RgbToHsv(in);

    hsv[1] += (saturation_change - 50)*0.02;
    if (hsv[1] < 0){
       hsv[1] = 0;
    }

    if (hsv[1] > 1){
        hsv[1] = 1;
    }
    uchar4 output = HsvToRgb(hsv);
    return output;
}