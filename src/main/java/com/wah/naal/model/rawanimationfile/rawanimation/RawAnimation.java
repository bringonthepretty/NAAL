package com.wah.naal.model.rawanimationfile.rawanimation;

import com.wah.naal.model.rawanimationfile.bone.Bone;
import com.wah.naal.model.rawanimationfile.framedata.PositionFrameData;

import java.util.List;
import java.util.Objects;

/**
 * This class represents raw animation
 */
public class RawAnimation {
    private String name;

    private List<Bone> bones;
    private List<PositionFrameData> locationFrameDataList;

    public RawAnimation(String name, List<Bone> bones, List<PositionFrameData> locationFrameDataList) {
        this.name = name;
        this.bones = bones;
        this.locationFrameDataList = locationFrameDataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bone> getBones() {
        return bones;
    }

    public void setBones(List<Bone> bones) {
        this.bones = bones;
    }

    public List<PositionFrameData> getLocationFrameDataList() {
        return locationFrameDataList;
    }

    public void setLocationFrameDataList(List<PositionFrameData> locationFrameDataList) {
        this.locationFrameDataList = locationFrameDataList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RawAnimation rawAnimation = (RawAnimation) o;

        if (!Objects.equals(name, rawAnimation.name)) return false;
        if (!Objects.equals(bones, rawAnimation.bones)) return false;
        return Objects.equals(locationFrameDataList, rawAnimation.locationFrameDataList);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (bones != null ? bones.hashCode() : 0);
        result = 31 * result + (locationFrameDataList != null ? locationFrameDataList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Naal{" +
                "name='" + name + '\'' +
                ", bones=" + bones +
                ", locationFrameDataList=" + locationFrameDataList +
                '}';
    }
}
