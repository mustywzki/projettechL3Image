#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed


uchar4 RS_KERNEL Sepia(uchar4 in) {
        float4 tab = rsUnpackColor8888(in);
        float red = tab[0]*0.393 + tab[1]*0.769 + tab[2]*0.189;
        float green = tab[0]*0.349 + tab[1]*0.686 + tab[2]*0.168;
        float blue = tab[0]*0.272 + tab[1]*0.534 + tab[2]*0.131;

        return rsPackColorTo8888(red, green, blue, tab[3]);
}