#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"
#pragma rs_fp_relaxed
float brightness_change;

uchar4 RS_KERNEL change_brightness(uchar4 in){
        float4 hsv = RgbToHsv(in);
        hsv[2] += (brightness_change - 50)*0.02;
            if (hsv[2] < 0){
                hsv[2] = 0;
            }
            if (hsv[2] > 1){
                hsv[2] = 1;
            }
        uchar4 output = HsvToRgb(hsv);
        return output;
    }