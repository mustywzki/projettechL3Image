#pragma version(1)
#pragma rs java_package_name(com.mustywzki.projettechl3image)

float rand;

float4 RS_KERNEL Colorize(float4 in){

    in.s0 = rand;

return in;
}