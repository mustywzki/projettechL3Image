#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed


uchar4 RS_KERNEL Sepia(uchar4 in) {
        float4 tab = rsUnpackColor8888(in);
        float red = tab[0]*0.393 + tab[1]*0.769 + tab[2]*0.189;
        float green = tab[0]*0.349 + tab[1]*0.686 + tab[2]*0.168;
        float blue = tab[0]*0.272 + tab[1]*0.534 + tab[2]*0.131;
        if (red < 0) red = 0;
        if (red > 255) red = 255;
        if (green < 0) green = 0;
        if (green > 255) green = 255;
        if (blue < 0) blue = 0;
        if (blue > 255) blue = 255;

        return rsPackColorTo8888(red, green, blue, tab[3]);
}