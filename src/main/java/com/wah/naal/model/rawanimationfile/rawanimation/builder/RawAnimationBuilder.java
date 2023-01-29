package com.wah.naal.model.rawanimationfile.rawanimation.builder;

import com.wah.naal.model.rawanimationfile.bone.Bone;
import com.wah.naal.model.rawanimationfile.rawanimation.RawAnimation;
import com.wah.naal.model.rawanimationfile.framedata.PositionFrameData;
import com.wah.naal.model.rawanimationfile.framedata.RotationFrameData;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents builder for {@link RawAnimation}
 */
public class RawAnimationBuilder {

    private static RawAnimationBuilder instance;

    private RawAnimationBuilder() {}

    /**
     * Returns instance of {@link RawAnimation}
     * @return instance of {@link RawAnimation}
     */
    public static RawAnimationBuilder getInstance() {
        if (Objects.isNull(instance)) {
            instance = new RawAnimationBuilder();
        }
        return instance;
    }

    public RawAnimation build(String source, String fileName) {
        List<Bone> bones = new ArrayList<>();
        source.lines().forEach(line -> {
            List<RotationFrameData> entries = new ArrayList<>();
            String[] array = StringUtils.substringsBetween(line, "<Quaternion ", ">");

            if (Objects.nonNull(array)) {
                for (String s: array) {
                    float w = Float.parseFloat(StringUtils.substringBetween(s, "w=", ","));
                    float rX = Float.parseFloat(StringUtils.substringBetween(s, "x=", ","));
                    float rY = Float.parseFloat(StringUtils.substringBetween(s, "y=", ","));
                    float rZ = Float.parseFloat(StringUtils.substringBetween(s, "z=", ")"));
                    entries.add(new RotationFrameData(w, rX, rY, rZ));
                }
                bones.add( new Bone(StringUtils.substringBefore(line, ":"), entries));
            }
        });
        List<PositionFrameData> locationFrameDataList = new ArrayList<>();

        String locationDataLine = source.lines().findFirst().orElse("");
        String[] locaionsAsString = StringUtils.substringsBetween(locationDataLine, "<Vector ", ">");
        for (String s: locaionsAsString) {
            float x = Float.parseFloat(StringUtils.substringBetween(s, "(", ", "));
            float y = Float.parseFloat(StringUtils.substringBetween(s, ", ", ", "));
            s = StringUtils.substringAfter(s, ", ");
            float z = Float.parseFloat(StringUtils.substringBetween(s, ", ", ")"));
            PositionFrameData entry = new PositionFrameData(x, y, z);
            locationFrameDataList.add(entry);
        }

        return new RawAnimation(fileName.replaceAll(".ran", ""), bones, locationFrameDataList);
    }

}
