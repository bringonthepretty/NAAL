package com.wah.naal.model.bvhfile.bvh.builder;

import com.wah.naal.model.bvhfile.bvh.Bvh;
import com.wah.naal.model.bvhfile.framedata.FrameData;
import com.wah.naal.model.bvhfile.joint.Joint;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents builder for BioVision motion capture {@link Bvh}
 * This class is singleton
 */
public class BvhBuilder {

    private static BvhBuilder instance;

    private BvhBuilder() {}

    /**
     * Returns instance of {@link BvhBuilder}
     * @return instance of {@link BvhBuilder}
     */
    public static synchronized BvhBuilder getInstance() {
        if (Objects.isNull(instance)) {
            instance = new BvhBuilder();
        }
        return instance;
    }

    /**
     * Builds {@link Bvh} from source string
     * @param source bvh data as string
     * @param fileName file name
     * @return {@link Bvh} object
     */
    public Bvh build(String source, String fileName) {
        Bvh result = new Bvh();
        result.setName(fileName.replace(".bvh", ""));

        String hierarchyString = StringUtils.removeStart(source, "HIERARCHY\n" + "ROOT __0");
        hierarchyString = StringUtils.substringBefore(hierarchyString, "\nMOTION");
        hierarchyString = StringUtils.removeStart(hierarchyString, "\n{\n");
        hierarchyString = StringUtils.removeEnd(hierarchyString, "\n}");

        String dataString = StringUtils.substringAfter(source, "Frame Time");
        dataString = StringUtils.substringAfter(dataString, "\n");

        String[] offsets = StringUtils.substringBetween(hierarchyString, "OFFSET ", "\n").split(" ");
        result.setOffsetX(Float.parseFloat(offsets[0]));
        result.setOffsetY(Float.parseFloat(offsets[1]));
        result.setOffsetZ(Float.parseFloat(offsets[2]));

        int frameCount = Integer.parseInt(StringUtils.substringBetween(source, "Frames: ", "\n"));
        result.setFrameCount(frameCount);

        List<Queue<Float>> data = new ArrayList<>();
        String[] frameStrings = dataString.split("\n");

        for (String frameString : frameStrings) {
            Queue<Float> frame = new ArrayDeque<>();
            String[] values = frameString.split(" ");
            for (String value : values) {
                frame.add(Float.parseFloat(value));
            }
            data.add(frame);
        }

        result.setJoints(parseJoints(StringUtils.substringAfter(hierarchyString, "CHANNELS 0\n"), data));

        return result;
    }

    private List<Joint> parseJoints(String hierarchyString, List<Queue<Float>> data) {
        List<Joint> joints = new ArrayList<>();

        AtomicInteger bracketsCounter = new AtomicInteger(0);
        StringBuilder jointHierarchyStringBuilder = new StringBuilder();
        hierarchyString.chars().forEach(aChar -> {

            jointHierarchyStringBuilder.append((char)aChar);

            if (aChar == '{') {
                bracketsCounter.incrementAndGet();
            }

            if (aChar == '}') {
                bracketsCounter.decrementAndGet();

                if (bracketsCounter.get() == 0) {
                    String jointHierarchyString = jointHierarchyStringBuilder.toString();

                    Joint joint = new Joint();

                    String[] offsets = StringUtils.substringBetween(jointHierarchyString, "OFFSET ", "\n").split(" ");
                    joint.setOffsetX(Float.parseFloat(offsets[0]));
                    joint.setOffsetY(Float.parseFloat(offsets[1]));
                    joint.setOffsetZ(Float.parseFloat(offsets[2]));

                    if (jointHierarchyString.contains("JOINT")) {
                        joint.setName(StringUtils.substringBetween(jointHierarchyString, "JOINT ", "\n"));

                        List<FrameData> jointFrameData = new ArrayList<>();

                        data.forEach(entry -> {
                            FrameData frameData = new FrameData();

                            frameData.setPositionX(entry.poll());
                            frameData.setPositionY(entry.poll());
                            frameData.setPositionZ(entry.poll());

                            frameData.setRotationX(entry.poll());
                            frameData.setRotationY(entry.poll());
                            frameData.setRotationZ(entry.poll());

                            jointFrameData.add(frameData);
                        });

                        joint.setFrameData(jointFrameData);
                    } else {
                        joint.setName("End Site");
                    }

                    jointHierarchyString = StringUtils.substringAfter(jointHierarchyString, "{\n");
                    jointHierarchyString = StringUtils.removeEnd(jointHierarchyString, "}");

                    jointHierarchyString = StringUtils.substringAfter(jointHierarchyString, "Zrotation\n");

                    joint.setChildren(parseJoints(jointHierarchyString, data));

                    joints.add(joint);
                    jointHierarchyStringBuilder.setLength(0);
                }
            }
        });

        return joints;
    }
}
