package com.wah.naal.model.bvhfile.joint;

import com.wah.naal.model.bvhfile.framedata.FrameData;
import com.wah.naal.model.bvhfile.bvh.Bvh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent {@link  Bvh}'s joint
 */
public class Joint {
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
