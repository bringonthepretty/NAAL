package com.wah.naal.model.rawanimationfile.framedata;

/**
 * This class represents {@link com.wah.naal.model.rawanimationfile.bone.Bone}'s rotation data for one frame in quaternions way
 */
public class RotationFrameData {
    private Float w;
    private Float x;
    private Float y;
    private Float z;

    public RotationFrameData(Float w, Float x, Float y, Float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Float getW() {
        return w;
    }

    public void setW(Float w) {
        this.w = w;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "NaalRotationFrameData{" + w + ", " + x + ", " + y + ", " + z + '}';
    }
}
