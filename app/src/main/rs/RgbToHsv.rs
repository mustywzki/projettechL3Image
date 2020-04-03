#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

uchar4  RS_KERNEL  rgb_to_hsv(uchar4  in) {

  
    float4 pixelf = rsUnpackColor8888(in);
    uchar4 tempP;
    uchar min = in.r; //min( in.r, min( in.g, in.b ) );
    uchar max = in.g;//max( in.r, max( in.g, in.b ) );
    float red = pixelf.r;
    float blue = pixelf.b;
    float green = pixelf.g;


    if(max==min){
        tempP.s0=0;
    }
    else if (max== red){
        tempP.s0 = fmod(((60 * (green - blue) / (max - min)) + 360),360);
    } 
    else if (max==green){
        tempP.s0 = (60 * (blue - red) / (max - min)) + 120;
    }
    else if(max==blue){
        tempP.s0= (60 * (red - green) / (max - min)) + 240;
    }
    
    if(max == 0){
        tempP.s1 = 0;
    }
    else{
        tempP.s1 = 1 - (min/max);
    }

    tempP.s2 = max;
    tempP.s3 = in.a;

    return tempP;
}