#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"


uchar4 RS_KERNEL negative(uchar4 in){
        uchar red = 255 - in.r;
        uchar green = 255 - in.g;
        uchar blue = 255 - in.b;
        uchar4 output = {red,green,blue, in[3]};
        return output;
    }