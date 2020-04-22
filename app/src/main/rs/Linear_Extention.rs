#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

int LUTred[256];
int LUTgreen[256];
int LUTblue[256];


uchar4 RS_KERNEL linear_extention(uchar4 in) {
            uchar4 output;
            output[0] = (uchar) LUTred[(int)in[0]];
            output[1] = (uchar) LUTgreen[(int)in[1]];
            output[2]= (uchar) LUTblue[(int)in[2]];
            output[3] = in[3];

        return output;
    }