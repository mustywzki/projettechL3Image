#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed
#include "RgbToHsv.rs"
#include "HsvToRgb.rs"

//On déclare un tableau de 256 valeurs
int32_t histogram[256];

void init_histogram() {
	for(int i=0;i<256;i++)
		histogram[i] = 0;
}


 void  RS_KERNEL AccumHisto(uchar4 in) {
        float4 hsv = RgbToHsv(in);
        int value =(int) (hsv[2] * (float)10);
        rsAtomicInc(&histogram[value]);
}