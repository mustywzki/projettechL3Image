#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"

//On déclare un tableau de 256 valeurs
// Note that this kernel returns an array to Java


#define ARRAY_SIZE 256
typedef int32_t Histogram[ARRAY_SIZE];


#pragma rs reduce(histogramm) \
  accumulator(hsgAccum) combiner(hsgCombine)

static void hsgAccum(Histogram *h, uchar4 in) {
    float4 hsv = RgbToHsv(in);
    uchar value =(uchar) (hsv[2] * (float)255);
    ++(*h)[value];
}


static void hsgCombine(Histogram *accum, const Histogram *addend) {
    for (int i = 0; i < ARRAY_SIZE; ++i)
        (*accum)[i] += (*addend)[i];
}

