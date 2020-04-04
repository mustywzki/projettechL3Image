#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

float4  RS_KERNEL RgbToHsv(uchar4  rgb_tab) {


    float4 rgb = rsUnpackColor8888(rgb_tab);
    float red = rgb.r;
    float blue = rgb.b;
    float green = rgb.g;
    float minimum = min( red, min( green, blue ) );
    float maximum = max( red, max( green, blue ) );



    if(maximum==minimum){
        rgb.s0=0;
    }
    else if (maximum== red){
        rgb.s0 = fmod(((60 * (green - blue) / (maximum - minimum)) + 360),360);
    }
    else if (maximum==green){
        rgb.s0 = (60 * (blue - red) / (maximum - minimum)) + 120;
    }
    else if(maximum==blue){
        rgb.s0= (60 * (red - green) / (maximum - minimum)) + 240;
    }

    if(maximum == 0){
        rgb.s1 = 0;
    }
    else{
        rgb.s1 = 1 - (minimum/maximum);
    }

    rgb.s2= maximum;

    return rgb;
}