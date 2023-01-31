package com.wah.naal.model.rawanimationfile.rawanimation.builder;

import com.wah.naal.model.rawanimationfile.bone.Bone;
import com.wah.naal.model.rawanimationfile.rawanimation.RawAnimation;
import com.wah.naal.model.rawanimationfile.framedata.PositionFrameData;
import com.wah.naal.model.rawanimationfile.framedata.RotationFrameData;
import com.wah.naal.exception.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents builder for {@link RawAnimation}
 */
public class RawAnimationBuilder {

    private static RawAnimationBuilder instance;

    private static final Logger logger = LoggerFactory.getLogger(RawAnimationBuilder.class);

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

    /**
     * Converts provided file data to {@link RawAnimation}
     * @param source file data as string
     * @param fileName file name
     * @throws ParseException when parsing error occurs
     * @return {@link RawAnimation}
     */
    public RawAnimation build(String source, String fileName) {
        List<Bone> bones = new ArrayList<>();
        List<PositionFrameData> locationFrameDataList = new ArrayList<>();

        if (source.isEmpty()) {
            throw new ParseException("File " + fileName + " is empty");
        }

        source.lines().skip(1).forEach(line -> {
            List<RotationFrameData> entries = new ArrayList<>();
            String[] array = StringUtils.substringsBetween(line, "<Quaternion ", ">");

            if (Objects.nonNull(array)) {
                for (String s: array) {
                    float w;
                    float rX;
                    float rY;
                    float rZ;

                    try {
                        w = Float.parseFloat(StringUtils.substringBetween(s, "w=", ","));
                        rX = Float.parseFloat(StringUtils.substringBetween(s, "x=", ","));
                        rY = Float.parseFloat(StringUtils.substringBetween(s, "y=", ","));
                        rZ = Float.parseFloat(StringUtils.substringBetween(s, "z=", ")"));
                    } catch (NumberFormatException e) {
                        throw new ParseException("Raw animation parse exception: rotation is corrupted: " + e.getMessage());
                    } catch (NullPointerException e) {
                        throw new ParseException("Raw animation parse exception: rotation is corrupted");
                    }

                    if (w > 1 || w < -1 || rX > 1 || rX < -1 || rY > 1 || rY < -1 || rZ > 1 || rZ < -1) {
                        logger.warn("Rotation: w" + w + " x" + rX + " y" + rY + " z" + rZ + " might be invalid: Quaternion rotation should be normalized");
                    }

                    entries.add(new RotationFrameData(w, rX, rY, rZ));
                }
                bones.add( new Bone(StringUtils.substringBefore(line, ":"), entries));
            } else {
                throw new ParseException("Raw animation parse exception: rotation is corrupted");
            }
        });

        String locationDataLine = source.lines().findFirst().orElse("");
        String[] locaionsAsString = StringUtils.substringsBetween(locationDataLine, "<Vector ", ">");

        if (Objects.nonNull(locaionsAsString)) {
            for (String s: locaionsAsString) {
                float x;
                float y;
                float z;
                try {
                    x = Float.parseFloat(StringUtils.substringBetween(s, "(", ", "));
                    y = Float.parseFloat(StringUtils.substringBetween(s, ", ", ", "));
                    s = StringUtils.substringAfter(s, ", ");
                    z = Float.parseFloat(StringUtils.substringBetween(s, ", ", ")"));
                } catch (NumberFormatException e) {
                    throw new ParseException("Raw animation parse exception: position is corrupted: " + e.getMessage());
                } catch (NullPointerException e) {
                    throw new ParseException("Raw animation parse exception: position is corrupted");
                }
                PositionFrameData entry = new PositionFrameData(x, y, z);
                locationFrameDataList.add(entry);
            }
        } else {
            throw new ParseException("Raw animation parse exception: position is corrupted");
        }

        return new RawAnimation(fileName.replaceAll(".ran", ""), bones, locationFrameDataList);
    }

}
