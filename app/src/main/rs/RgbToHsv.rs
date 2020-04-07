#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed


float4  RS_KERNEL RgbToHsv(uchar4  rgb_tab){

     float4 rgba = rsUnpackColor8888(rgb_tab);
     float4 hsv;
     float maximum =max(rgba[0], max(rgba[1],rgba[2]));
     float minimum =min(rgba[0], min(rgba[1],rgba[2]));
     float delta = maximum - minimum;
     float red = rgba[0];
     float green = rgba[1];
     float blue = rgba[2];

            /* --- Hue calculation --- */
            if (delta == 0){
                hsv[0] = 0;
            }
            else if (maximum == red){
                hsv[0] = fmod(((green - blue)/delta),6);
            }
            else if (maximum == green){
                hsv[0] = (blue - red)/delta + 2;
            }
            else if (maximum == blue){
                hsv[0] = (red - green)/delta + 4;
            }
            hsv[0] = round(hsv[0] * 60);

            if (hsv[0] < 0){
                hsv[0] += 360;
            }

            /* --- Saturation calculation --- */
            if (maximum == 0){
                hsv[1] = 0;
            }
            else{
                hsv[1] = delta / maximum;
            }

            /* --- Value calculation --- */
            hsv[2] = maximum;
            hsv[3] = rgba[3];

            return hsv;
}