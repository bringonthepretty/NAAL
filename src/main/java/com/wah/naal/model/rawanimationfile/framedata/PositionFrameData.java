package com.wah.naal.model.rawanimationfile.framedata;

import java.util.Objects;

/**
 * This class represents {@link com.wah.naal.model.rawanimationfile.rawanimation.RawAnimation}'s position data for one frame
 */
public class PositionFrameData {

    private Float x;
    private Float y;
    private Float z;

    public PositionFrameData(Float x, Float y, Float rotationZ) {
        this.x = x;
        this.y = y;
        this.z = rotationZ;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionFrameData that = (PositionFrameData) o;

        if (!Objects.equals(x, that.x)) return false;
        if (!Objects.equals(y, that.y)) return false;
        return Objects.equals(z, that.z);
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NaalPositionFrameData{" + x + ", " + y + ", " + z + '}';
    }
}
