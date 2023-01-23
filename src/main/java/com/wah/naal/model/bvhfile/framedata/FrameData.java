package com.wah.naal.model.bvhfile.framedata;

import com.wah.naal.model.bvhfile.joint.Joint;

/**
 * This class represents data for one frame for {@link Joint}
 */
public class FrameData {
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
