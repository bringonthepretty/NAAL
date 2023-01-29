package com.wah.naal.model.rawanimationfile.bone;

import com.wah.naal.model.rawanimationfile.framedata.RotationFrameData;

import java.util.List;
import java.util.Objects;

/**
 * This class represents {@link com.wah.naal.model.rawanimationfile.rawanimation.RawAnimation}'s bone
 */
public class Bone {
    private String name;

    private List<RotationFrameData> frameDataList;

    public Bone(String name, List<RotationFrameData> frameDataList) {
        this.name = name;
        this.frameDataList = frameDataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RotationFrameData> getFrameDataList() {
        return frameDataList;
    }

    public void setFrameDataList(List<RotationFrameData> frameDataList) {
        this.frameDataList = frameDataList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bone bone = (Bone) o;

        if (!Objects.equals(name, bone.name)) return false;
        return Objects.equals(frameDataList, bone.frameDataList);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (frameDataList != null ? frameDataList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bone{name= " + name + ", frameDataList= " + frameDataList + '}';
    }
}
