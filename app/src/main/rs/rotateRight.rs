#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

rs_allocation gOut;
int height, width;

void RS_KERNEL rotateRight(uchar4 in, uint32_t x, uint32_t y){
    rsSetElementAt_uchar4(gOut,in, width-(height-y-1)-1,width-x-1);
}