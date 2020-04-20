#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed


rs_allocation A1, A2;

uchar4  RS_KERNEL mix_bmp(uchar4 in , uint32_t x, uint32_t y ) {
        float4 pixelA1 = rsUnpackColor8888(rsGetElementAt_uchar4(A1,x,y));
        float4 pixelA2 = rsUnpackColor8888(rsGetElementAt_uchar4(A2,x,y));


            float red1 = pixelA1[0];
            float red2 = pixelA2[0];

            float green1 = pixelA1[1];
            float green2 = pixelA2[1];

            float blue1 = pixelA1[2];
            float blue2 = pixelA2[2];

            float red = (red1 + red2)/2;
            float green = (green1 + green2)/2;
            float blue = (blue1+blue2)/2;

            float3 output = {red, green, blue};
            return rsPackColorTo8888(output);
    }