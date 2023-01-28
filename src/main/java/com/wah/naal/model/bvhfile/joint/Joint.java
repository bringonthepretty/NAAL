package com.wah.naal.model.bvhfile.joint;

import com.wah.naal.model.bvhfile.framedata.FrameData;
import com.wah.naal.model.bvhfile.bvh.Bvh;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represent {@link  Bvh}'s joint
 */
public class Joint implements Cloneable{
    private String name;

    private Float offsetX;
    private Float offsetY;
    private Float offsetZ;

    private List<FrameData> frameData;

    private List<Joint> children;

    public Joint() {

    }

    public List<Joint> getAllJointsAsList() {
        List<Joint> result = new ArrayList<>(children);
        children.forEach(child -> result.addAll(child.getAllJointsAsList()));
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getOffsetX() {
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

    public List<FrameData> getFrameData() {
        return frameData;
    }

    public void setFrameData(List<FrameData> frameData) {
        this.frameData = frameData;
    }

    public List<Joint> getChildren() {
        return children;
    }

    public void setChildren(List<Joint> children) {
        this.children = children;
    }

    @Override
    public Joint clone() {
        Joint clone = new Joint();

        clone.setName(name);

        clone.setOffsetX(offsetX);
        clone.setOffsetY(offsetY);
        clone.setOffsetZ(offsetZ);

        List<Joint> clonedChildren = new ArrayList<>();

        children.forEach(child -> clonedChildren.add(child.clone()));

        clone.setChildren(clonedChildren);

        List<FrameData> clonedFrameData = new ArrayList<>();

        if (Objects.nonNull(frameData)) {
            frameData.forEach(entry -> clonedFrameData.add(entry.clone()));
            clone.setFrameData(clonedFrameData);
        }

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Joint joint = (Joint) o;

        if (!Objects.equals(name, joint.name)) return false;
        if (!Objects.equals(offsetX, joint.offsetX)) return false;
        if (!Objects.equals(offsetY, joint.offsetY)) return false;
        if (!Objects.equals(offsetZ, joint.offsetZ)) return false;
        if (!Objects.equals(frameData, joint.frameData)) return false;
        return Objects.equals(children, joint.children);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (offsetX != null ? offsetX.hashCode() : 0);
        result = 31 * result + (offsetY != null ? offsetY.hashCode() : 0);
        result = 31 * result + (offsetZ != null ? offsetZ.hashCode() : 0);
        result = 31 * result + (frameData != null ? frameData.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!children.isEmpty()){
            children.forEach(child -> stringBuilder.append("\n").append(child));
            stringBuilder.append("\n");
        }

        return "Joint{" +
                "name='" + name + '\'' +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", offsetZ=" + offsetZ +
                ", frameData=" + frameData +
                ", children=" + (!stringBuilder.isEmpty() ? stringBuilder : null) +
                '}';
    }
}
