#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed


uchar4 RS_KERNEL Gray(uchar4 in) {

        float red = 0.3;
        float blue = 0.11;
        float green = 0.59;

        float4 tab = rsUnpackColor8888(in);

        float gray = red * tab[0];
        gray += green * tab[1];
        gray += blue * tab[2];

        return rsPackColorTo8888(gray, gray, gray, tab[3]);
}