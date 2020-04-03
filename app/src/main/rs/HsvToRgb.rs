#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

uchar4  RS_KERNEL  HsvToRgb(uchar4  in) {
    uchar h = in.s0;
    uchar s = in.s1;
    uchar v = in.s2;
    uchar alpha = in.s3;

    uchar t = (h / 60) % 6;
    uchar f = (h / 60) - t;
    uchar l = v * (1 - s);
    uchar m =  (v * (1 - (f - s)));
    uchar n = v * (1 - (1 - f) * s);

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