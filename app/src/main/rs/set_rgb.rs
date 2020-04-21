#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed

float red_coef;
float green_coef;
float blue_coef;


uchar4 RS_KERNEL setRgb(uchar4 in) {
        float4 pixel = rsUnpackColor8888(in);
        float new_red_c = (red_coef - 50)*0.02;
        float new_green_c = (green_coef - 50)*0.02;
        float new_blue_c = (blue_coef - 50)*0.02;

            float redValue = in[0];
            redValue += 255*new_red_c;

            if (redValue < 0) redValue = 0;
            if (redValue > 255) redValue = 255;

            float greenValue = in[1];
            greenValue +=  255* new_green_c;
            if (greenValue < 0) greenValue = 0;
            if (greenValue > 255) greenValue = 255;

            float blueValue = in[2];
            blueValue +=  255*new_blue_c;
            if (blueValue < 0) blueValue = 0;
            if (blueValue > 255) blueValue = 255;

            uchar4 output = {redValue, greenValue, blueValue, in[3] };
            return output;
}