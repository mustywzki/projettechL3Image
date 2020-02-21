#pragma version(1)
#pragma rs java_package_name(com.mustywzki.imageandroidproj)

double newr, newg, newb;

uchar4 RS_KERNEL toGray(uchar4 in) {

    const uchar gray = (newr*in.r
                     + newg*in.g
                     + newb*in.b)/100;

    return (uchar4){gray, gray, gray, in.a};

}