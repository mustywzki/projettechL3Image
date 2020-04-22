#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed

rs_allocation gIn;
int width;

uchar4 RS_KERNEL reverseHor(uchar4 in, uint32_t x, uint32_t y) {
    return rsGetElementAt_uchar4(gIn, width - x - 1, y);

}