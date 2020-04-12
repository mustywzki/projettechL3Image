#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

float red_coef, blue_coef, green_coef;

uchar4 RS_KERNEL Gray(uchar4 in) {

        float red= red_coef;
        float blue= blue_coef;
        float green = green_coef;

        if( red > 1){
            red = 1;
         }

        if(red< 0){
            red = 0;
          }

        if( blue > 1){
            blue = 1;
        }

        if(blue < 0){
            blue = 0;
        }

        if( green > 1){
            green = 1;
        }

        if(green < 0){
            green = 0;
        }

        float4 tab = rsUnpackColor8888(in);

        float gray = red * tab[0];
        gray += blue * tab[1];
        gray += green * tab[2];

        float4 out = {gray , gray, gray , tab[3]};
        uchar4 output = rsPackColorTo8888 (out );
        return output;
}