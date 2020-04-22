#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed

int width, height;

float* core;
int core_length;
float div;
rs_allocation gIn;


uchar4 RS_KERNEL apply_filter(uchar4 in, uint32_t x, uint32_t y){

    // Set la longueur d'une ligne dans le carré du filtre
    int line = sqrt((float)core_length);

    int dif = (int) (line - 1) / 2;

    float newRed, newGreen, newBlue;

    //id_color_to_set = (y * width) + x ; // indice du pixel courant à modifier
    newRed = 0;
    newGreen = 0;
    newBlue = 0;

            // Parcourir le carré autour du pixel courant (en fonction de la taille de core)
            for (int j = 0; j < line; j++)
            {
                for (int i = 0; i < line; i++)
                {
                    //id_color = id_color_to_set + (j - dif) * width + (i - dif); // indice de la couleur dans le carré de l'image
                    int new_y= y+(j-dif);
                    int new_x = x + (i-dif);

                    int id_core = (int) (j * line + i); // indice de la couleur dans le carré du filtre
                    if(new_x>=0 && new_y>=0){
                    uchar4 pixel = rsGetElementAt_uchar4(gIn,new_x,new_y);
                    float4 p =rsUnpackColor8888(pixel);
                    newRed = newRed + (p[0] * core[id_core]);

                    newGreen = newGreen + (p[1] * core[id_core]);

                    newBlue = newBlue + (p[2] * core[id_core]);
                    }
                }
            }
            float red = newRed / (float) div;
            float green =  newGreen / (float) div;
            float blue =  newBlue / (float) div;
            red = fabs(red);
            green = fabs(green);
            blue = fabs(blue);
            float alpha = (rsUnpackColor8888(in))[3];

            red = red > 255 ? 255 : red;
            green = green > 255 ? 255 : green;
            blue = blue > 255 ? 255 : blue;
            red = red < 0 ? 0 : red;
            green = green < 0 ? 0 : green;
            blue = blue < 0 ? 0 : blue;

            return rsPackColorTo8888( red, green,blue,alpha );

}
