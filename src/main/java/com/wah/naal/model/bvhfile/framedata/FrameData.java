package com.wah.naal.model.bvhfile.framedata;

import com.wah.naal.model.bvhfile.joint.Joint;

import java.util.Objects;

/**
 * This class represents data for one frame for {@link Joint}
 */
public class FrameData implements Cloneable{
    private Float positionX;
    private Float positionY;
    private Float positionZ;

    private Float rotationX;
    private Float rotationY;
    private Float rotationZ;

    public Float getPositionX() {
        return positionX;
    }

    public void setPositionX(Float positionX) {
        this.positionX = positionX;
    }

    public Float getPositionY() {
        return positionY;
    }

    public void setPositionY(Float positionY) {
        this.positionY = positionY;
    }

    public Float getPositionZ() {
        return positionZ;
    }

    public void setPositionZ(Float positionZ) {
        this.positionZ = positionZ;
    }

    public Float getRotationX() {
        return rotationX;
    }

    public void setRotationX(Float rotationX) {
        this.rotationX = rotationX;
    }

    public Float getRotationY() {
        return rotationY;
    }

    public void setRotationY(Float rotationY) {
        this.rotationY = rotationY;
    }

    public Float getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(Float rotationZ) {
        this.rotationZ = rotationZ;
    }

    @Override
    public FrameData clone() {
        FrameData clone = new FrameData();

        clone.setPositionX(positionX);
        clone.setPositionY(positionY);
        clone.setPositionZ(positionZ);

        clone.setRotationX(rotationX);
        clone.setRotationY(rotationY);
        clone.setRotationZ(rotationZ);

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FrameData frameData = (FrameData) o;

        if (!Objects.equals(positionX, frameData.positionX)) return false;
        if (!Objects.equals(positionY, frameData.positionY)) return false;
        if (!Objects.equals(positionZ, frameData.positionZ)) return false;
        if (!Objects.equals(rotationX, frameData.rotationX)) return false;
        if (!Objects.equals(rotationY, frameData.rotationY)) return false;
        return Objects.equals(rotationZ, frameData.rotationZ);
    }

    @Override
    public int hashCode() {
        int result = positionX != null ? positionX.hashCode() : 0;
        result = 31 * result + (positionY != null ? positionY.hashCode() : 0);
        result = 31 * result + (positionZ != null ? positionZ.hashCode() : 0);
        result = 31 * result + (rotationX != null ? rotationX.hashCode() : 0);
        result = 31 * result + (rotationY != null ? rotationY.hashCode() : 0);
        result = 31 * result + (rotationZ != null ? rotationZ.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FrameData{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", positionZ=" + positionZ +
                ", rotationX=" + rotationX +
                ", rotationY=" + rotationY +
                ", rotationZ=" + rotationZ +
                '}';
    }
}
