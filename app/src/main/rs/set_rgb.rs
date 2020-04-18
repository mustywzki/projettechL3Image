#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed

float red_coef;
float green_coef;
float blue_coef;


// TODO change strange
uchar4 RS_KERNEL setRgb(uchar4 in) {
    uchar4 out = in;

    out.r += 255*(red_coef - 50)*0.02;
    if (out.r < 0){
       out.r = 0;
    }
    if (out.r > 255){
        out.r = 255;
    }

    out.g += 255*(green_coef - 50)*0.02;
    if (out.g < 0){
        out.g = 0;
    }
    if (out.g > 255){
        out.g = 255;
    }

    out.b += 255*(blue_coef - 50)*0.02;
    if (out.b < 0){
        out.b = 0;
    }
    if (out.b > 255){
        out.b = 255;
    }

    return out;
}