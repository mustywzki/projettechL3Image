#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)
#pragma rs_fp_relaxed



#define INT_MAX 255
#define INT_MIN 0

#define ARRAY_SIZE 6
typedef int32_t tab[ARRAY_SIZE];


typedef struct {
    int max_r, min_r;
    int max_g, min_g;
    int max_b, min_b;
} MinAndMax;






#pragma rs reduce(MaxMin) \
  initializer(Init) accumulator(Accumulator) \
  combiner(Combiner) outconverter(OutConverter)



static void Init(MinAndMax *accum) {
    accum->max_r= INT_MIN;
    accum->min_r = INT_MAX;
    accum->max_g = INT_MIN;
    accum->min_g = INT_MAX;
    accum->max_b = INT_MIN;
    accum->min_b  = INT_MAX;
}

static void Accumulator(MinAndMax *accum, uchar4 in) {

        uchar red = in[0];
        uchar green = in[1];
        uchar blue = in[2];

        if (red > accum->max_r){
            accum->max_r = red;
         }
        if (red < accum->min_r){
            accum->min_r = red;
        }

        if (green > accum->max_g){
            accum->max_g = green;
        }
        if (green < accum->min_g){
            accum->min_g = green;
        }

        if (blue > accum->max_b){
            accum->max_b = blue;
        }
        if (blue < accum->min_b){
            accum->min_b = blue;
        }
}








static void Combiner(MinAndMax *accum, const MinAndMax *val) {
    if (accum->min_r > val->min_r){
        accum->min_r = val->min_r;
    }
    if (accum->max_r < val->max_r){
        accum->max_r = val->max_r;
    }


    if (accum->min_g > val->min_g){
        accum->min_g = val->min_g;
    }
    if (accum->max_g < val->max_g){
        accum->max_g = val->max_g;
    }

    if (accum->min_b > val->min_b){
        accum->min_b = val->min_b;
    }
    if (accum->max_b < val->max_b){
        accum->max_b = val->max_b;
    }

}



static void OutConverter(tab * result, const MinAndMax *val) {
    (*result)[0] = (uchar) val->max_r;
    (*result)[1]= (uchar) val->min_r;
    (*result)[2] = (uchar) val->max_g;
    (*result)[3] = (uchar) val->min_g;
    (*result)[4] = (uchar) val->max_b;
    (*result)[5] = (uchar) val->min_b;

}