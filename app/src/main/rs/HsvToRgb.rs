#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

uchar4  RS_KERNEL HsvToRgb(float4  hsv) {
    float h = hsv.s0;
    float s = hsv.s1;
    float v = hsv.s2;
    float alpha = hsv.s3;

    float t = fmod((h / 60),6);
    float f = (h / 60) - t;
    float l = v * (1 - s);
    float m =  (v * (1 - (f - s)));
    float n = v * (1 - (1 - f) * s);

        if (t == 0) {
            return  rsPackColorTo8888(v, n, l,alpha);
        } else if (t == 1) {
            return  rsPackColorTo8888(m, v, l,alpha);
        } else if (t == 2) {
            return  rsPackColorTo8888(l, v, n, alpha);
        } else if (t == 3) {
            return  rsPackColorTo8888(l, m, v,alpha);
        } else if (t == 4) {
            return  rsPackColorTo8888(n, l, v, alpha);
        }
        return  rsPackColorTo8888(v, l, m,alpha);

}