package com.wah.naal.model.bvhfile.bvh;

import com.wah.naal.model.bvhfile.joint.Joint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents BioVision motion capture file.
 */
@Deprecated(forRemoval = true)
public class Bvh implements Cloneable{
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
    public Bvh clone() {
        Bvh clone = new Bvh();

        clone.setName(name);

        clone.setOffsetX(offsetX);
        clone.setOffsetY(offsetY);
        clone.setOffsetZ(offsetZ);

        clone.setFrameCount(frameCount);

        List<Joint> clonedJoints = new ArrayList<>();

        joints.forEach(joint -> clonedJoints.add(joint.clone()));

        clone.setJoints(clonedJoints);

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bvh bvh = (Bvh) o;

        if (!Objects.equals(name, bvh.name)) return false;
        if (!Objects.equals(offsetX, bvh.offsetX)) return false;
        if (!Objects.equals(offsetY, bvh.offsetY)) return false;
        if (!Objects.equals(offsetZ, bvh.offsetZ)) return false;
        if (!Objects.equals(frameCount, bvh.frameCount)) return false;
        return Objects.equals(joints, bvh.joints);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (offsetX != null ? offsetX.hashCode() : 0);
        result = 31 * result + (offsetY != null ? offsetY.hashCode() : 0);
        result = 31 * result + (offsetZ != null ? offsetZ.hashCode() : 0);
        result = 31 * result + (frameCount != null ? frameCount.hashCode() : 0);
        result = 31 * result + (joints != null ? joints.hashCode() : 0);
        return result;
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
