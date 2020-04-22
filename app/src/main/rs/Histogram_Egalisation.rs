#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"
int cumulative_histogramm[256];
int tab_length;


uchar4 RS_KERNEL HistogramEqualize(uchar4 in) {
    float4 hsv = RgbToHsv(in);
    int x = cumulative_histogramm [ (int) (hsv[2] * (float)255)];
    hsv[2] = (float) (x * 255 / tab_length) / (float)255;
    uchar4 output = HsvToRgb(hsv);
    return output;
}