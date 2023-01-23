package com.wah.naal.model.bvhfile.bvh;

import com.wah.naal.model.bvhfile.joint.Joint;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents BioVision motion capture file.
 */
public class Bvh {
    private String name;
    private Float offsetX;
    private Float offsetY;
    private Float offsetZ;
    private Integer frameCount;
    private List<Joint> joints;

    public List<Joint> getAllJointsAsList() {
        List<Joint> result = new ArrayList<>(joints);
        joints.forEach(joint -> result.addAll(joint.getAllJointsAsList()));
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Float offsetX) {
        this.offsetX = offsetX;
    }

    public Float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Float offsetY) {
        this.offsetY = offsetY;
    }

    public Float getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(Float offsetZ) {
        this.offsetZ = offsetZ;
    }

    public Integer getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(Integer frameCount) {
        this.frameCount = frameCount;
    }

    public List<Joint> getJoints() {
        return joints;
    }

    public void setJoints(List<Joint> joints) {
        this.joints = joints;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        joints.forEach(child -> stringBuilder.append(child).append("\n"));

        return "Bvh{" +
                "name=" + name +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", offsetZ=" + offsetZ +
                ", frameCount=" + frameCount +
                ", joints=" + stringBuilder +
                '}';
    }
}
