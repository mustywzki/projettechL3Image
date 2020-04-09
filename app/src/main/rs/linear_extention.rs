#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

int LUTred[256];
int LUTgreen[256];
int LUTblue[256];


uchar4 RS_KERNEL linear_extention(uchar4 in) {

        //int LUTred[256]= createLUTred(Tools.max_min_red(pixels));
        //int[] LUTgreen = createLUTgreen(Tools.max_min_green(pixels));
        //int[] LUTblue = createLUTblue(Tools.max_min_blue(pixels));
            in.r = LUTred[in.r];
            in.g = LUTgreen[in.g];
            in.b = LUTblue[in.b];
        return in;
    }