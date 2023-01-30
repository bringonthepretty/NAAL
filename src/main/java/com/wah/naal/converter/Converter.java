package com.wah.naal.converter;

import com.wah.naal.io.FileIO;
import com.wah.naal.model.bvhfile.bvh.Bvh;
import com.wah.naal.model.bvhfile.joint.Joint;
import com.wah.naal.model.motionfile.motion.Motion;
import com.wah.naal.model.motionfile.record.Record;
import com.wah.naal.model.motionfile.value.api.Value;
import com.wah.naal.model.motionfile.value.impl.Value0;
import com.wah.naal.model.motionfile.value.impl.Value3;
import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents converter from BioVision motion capture {@link Bvh} to
 * Nier:Automata proprietary motion format {@link Motion}.
 * This class is singleton
 */
@Deprecated(forRemoval = true)
public class Converter {

    private static final Double DEGREE_TO_AUTOMATA_ROTATION_UNITS_RATIO = 0.0174565566508981d;
    private static final Double DEGREES_360_TO_AUTOMATA_ROTATION_UNITS_RATIO = 6.284360394323324d;

    private static final Double METER_TO_AUTOMATA_POSITION_UNITS_RATIO = 1d;

    private static final Float Y_OFFSET = 1.016f;

    //05 04 12 20
    //value from pl0100_0006.mot, seems to be same to some files, purpose is unknown
    private static final Long DEFAULT_MOTION_HEADER_UNKNOWN1 = 538051589L;
    //00 00 00 00
    //value from pl0100_0006.mot, seems to be same to some files, purpose is unknown
    private static final Long DEFAULT_MOTION_HEADER_UNKNOWN2 = 0L;
    //00 00
    //value from pl0100_0006.mot, seems to be same to some files, purpose is unknown
    private static final Integer DEFAULT_MOTION_HEADER_FLAGS = 0;
    //00 00
    //value from pl0100_0006.mot, seems to be same to some files, purpose is unknown
    private static final Integer DEFAULT_RECORD_HEADER_UNKNOWN = 0;

    private static final Integer VALUE_TYPE_3_MAX_NUMBER = 255;
    private static final Integer VALUE_TYPE_2_MAX_NUMBER = 65535;

    private static final Integer DEGREES_360 = 360;

    private static final List<Integer> allowedBones = new ArrayList<>();

    private static Map<Integer, Integer> bonesMap;

    private static Converter instance;

    private Converter() {
        bonesMap = FileIO.getInstance().loadBonesMap();

        for (int i = -1; i <= 22; i++) {
            allowedBones.add(i);
        }

        for (int i = 256; i <= 274; i++) {
            allowedBones.add(i);
        }

        for (int i = 512; i <= 530; i++) {
            allowedBones.add(i);
        }
        allowedBones.add(1536);
        allowedBones.add(32767);
    }

    /**
     * Returns instance of {@link Converter}
     * @return instance of {@link Converter}
     */
    public static synchronized Converter getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Converter();
        }
        return instance;
    }

    /**
     * Converts {@link Bvh} to {@link Motion}
     * @param source source bvh
     * @return converted {@link Motion}
     */
    public Motion convert(Bvh source) {
        Motion result = new Motion();
        Bvh clone = source.clone();
        convertBvhRotationOrderFromZXYToXYZ(clone);
        result.setRecords(getRecords(clone));
        setMotionHeaderValues(result, clone);
        result.getRecords().sort(Comparator.comparingInt(Record::getBoneIndex));
        return result;
    }

    private List<Record> getBaseBoneRotationDummy() {
        List<Record> result = new ArrayList<>();

        Record recordRotationX = new Record();
        Record recordRotationY = new Record();
        Record recordRotationZ = new Record();

        recordRotationX.setBoneIndex(-1);
        recordRotationY.setBoneIndex(-1);
        recordRotationZ.setBoneIndex(-1);

        recordRotationX.setValueIndex(3);
        recordRotationY.setValueIndex(4);
        recordRotationZ.setValueIndex(5);

        recordRotationX.setRecordType(0);
        recordRotationY.setRecordType(0);
        recordRotationZ.setRecordType(0);

        recordRotationX.setValueEntriesCount(0);
        recordRotationY.setValueEntriesCount(0);
        recordRotationZ.setValueEntriesCount(0);

        recordRotationX.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationY.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationZ.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);

        Value0 value0 = new Value0(0f);

        recordRotationX.setValue(value0);
        recordRotationY.setValue(value0);
        recordRotationZ.setValue(value0);

        result.add(recordRotationX);
        result.add(recordRotationY);
        result.add(recordRotationZ);

        return result;
    }

    private void setMotionHeaderValues(Motion target, Bvh source) {
        target.setFileDescription("mot");
        target.setUnknown1(DEFAULT_MOTION_HEADER_UNKNOWN1);
        target.setFlags(DEFAULT_MOTION_HEADER_FLAGS);
        target.setFrameCount(source.getFrameCount());
        target.setRecordsCount((long)target.getRecords().size());
        target.setUnknown2(DEFAULT_MOTION_HEADER_UNKNOWN2);
        target.setMotionName(source.getName().replace("pl0100","pl0000"));
    }

    private List<Record> getRecords(Bvh source) {
        List<Record> records = new ArrayList<>();
        List<Joint> joints = source.getAllJointsAsList();
        joints.forEach(joint -> records.addAll(convertJointToRotationsRecordsList(joint)));
        Joint joint0 = joints.stream().filter(joint -> joint.getName().equals("bone0") || joint.getName().equals("bone000")).findFirst().orElse(null);

        if (Objects.isNull(joint0)) {
            throw new RuntimeException("0 joint must be present");
        }

        records.addAll(convert0JointToPositionRecordList(joint0));

        records.addAll(getBaseBoneRotationDummy());

        return records;
    }

    private List<Record> convertJointToRotationsRecordsList(Joint joint) {
        List<Record> result = new ArrayList<>();

        int boneIndex;

        try {
            boneIndex = Integer.parseInt(joint.getName().replace("bone", ""));
        } catch (NumberFormatException e){
            return result;
        }

        boneIndex = bonesMap.getOrDefault(boneIndex, 0);

        if (!allowedBones.contains(boneIndex)) {
            return result;
        }

        Record recordRotationX = new Record();
        Record recordRotationY = new Record();
        Record recordRotationZ = new Record();

        recordRotationX.setBoneIndex(boneIndex);
        recordRotationY.setBoneIndex(boneIndex);
        recordRotationZ.setBoneIndex(boneIndex);

        recordRotationX.setValueIndex(3);
        recordRotationY.setValueIndex(4);
        recordRotationZ.setValueIndex(5);

        recordRotationX.setValueEntriesCount(joint.getFrameData().size());
        recordRotationY.setValueEntriesCount(joint.getFrameData().size());
        recordRotationZ.setValueEntriesCount(joint.getFrameData().size());

        recordRotationX.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationY.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationZ.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);

        Value valueX = generateRotationValue(getJointRotationAxisData(joint, 0));
        Value valueY = generateRotationValue(getJointRotationAxisData(joint, 1));
        Value valueZ = generateRotationValue(getJointRotationAxisData(joint, 2));

        recordRotationX.setValue(valueX);
        recordRotationY.setValue(valueY);
        recordRotationZ.setValue(valueZ);

        recordRotationX.setRecordType(valueX.getType());
        recordRotationY.setRecordType(valueY.getType());
        recordRotationZ.setRecordType(valueZ.getType());

        result.add(recordRotationX);
        result.add(recordRotationY);
        result.add(recordRotationZ);
        return result;
    }

    private List<Record> convert0JointToPositionRecordList(Joint joint) {
        List<Record> result = new ArrayList<>();

        Record recordPositionX = new Record();
        Record recordPositionY = new Record();
        Record recordPositionZ = new Record();

        recordPositionX.setBoneIndex(-1);
        recordPositionY.setBoneIndex(-1);
        recordPositionZ.setBoneIndex(-1);

        recordPositionX.setValueIndex(0);
        recordPositionY.setValueIndex(1);
        recordPositionZ.setValueIndex(2);

        recordPositionX.setValueEntriesCount(joint.getFrameData().size());
        recordPositionY.setValueEntriesCount(joint.getFrameData().size());
        recordPositionZ.setValueEntriesCount(joint.getFrameData().size());

        recordPositionX.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordPositionY.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordPositionZ.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);

        List<Float> xData = getJointPositionAxisData(joint, 0);
        List<Float> yData = getJointPositionAxisData(joint, 1);
        List<Float> zData = getJointPositionAxisData(joint, 2);

        yData = yData.stream().map(value -> value - Y_OFFSET).collect(Collectors.toCollection(ArrayList::new));

        Value valueX = generatePositionValue(xData);
        Value valueY = generatePositionValue(yData);
        Value valueZ = generatePositionValue(zData);

        recordPositionX.setValue(valueX);
        recordPositionY.setValue(valueY);
        recordPositionZ.setValue(valueZ);

        recordPositionX.setRecordType(valueX.getType());
        recordPositionY.setRecordType(valueY.getType());
        recordPositionZ.setRecordType(valueZ.getType());

        result.add(recordPositionX);
        result.add(recordPositionY);
        result.add(recordPositionZ);

        return result;
    }

    /**
     * Returns list of values for provided axis
     * @param joint source joint
     * @param axisIndex axis, 0 - X axis, 1 - Y axis, 2 - Z axis
     * @return list of values for provided axis
     */
    private List<Float> getJointRotationAxisData(Joint joint, int axisIndex) {
        return joint.getFrameData()
                .stream()
                .map(frameData ->
                        switch (axisIndex) {
                            case 1 -> frameData.getRotationY();
                            case 2 -> frameData.getRotationZ();
                            default -> frameData.getRotationX();
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns list of values for provided axis
     * @param joint source joint
     * @param axisIndex axis, 0 - X axis, 1 - Y axis, 2 - Z axis
     * @return list of values for provided axis
     */
    private List<Float> getJointPositionAxisData(Joint joint, int axisIndex) {
        return joint.getFrameData()
                .stream()
                .map(frameData ->
                        switch (axisIndex) {
                            case 1 -> frameData.getPositionY();
                            case 2 -> frameData.getPositionZ();
                            default -> frameData.getPositionX();
                        })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Value generateRotationValue(List<Float> data) {
        return generateValue(data, DEGREE_TO_AUTOMATA_ROTATION_UNITS_RATIO);
    }

    private Value generatePositionValue(List<Float> data) {
        return generateValue(data, METER_TO_AUTOMATA_POSITION_UNITS_RATIO);
    }

    private Value generateValue(List<Float> data, Double ratio) {
        if (data.stream().allMatch(value -> value == 0f)) {
            return new Value0(0f);
        }

        Float maxValueEntryNormalized;
        Float minValueEntry = data.stream().min(Float::compareTo).orElse(0f);
        Float valueDelta;
        Value3 value3 = new Value3();

        data = data.stream().map(value -> value - minValueEntry).collect(Collectors.toCollection(ArrayList::new));
        value3.setValue((float) (minValueEntry * ratio));

        maxValueEntryNormalized = data.stream().max(Float::compareTo).orElse(0f);
        valueDelta = (float) (maxValueEntryNormalized * ratio / VALUE_TYPE_3_MAX_NUMBER);
        value3.setValueDelta(valueDelta);
        Float valueEntryToByteNumber = VALUE_TYPE_3_MAX_NUMBER / maxValueEntryNormalized;

        value3.setEntries(data.stream()
                .map(frameDegree -> {
                    int value = (int)(frameDegree * valueEntryToByteNumber);
                    value = value % (VALUE_TYPE_3_MAX_NUMBER + 1);
                    if(value == Integer.MAX_VALUE) {
                        value = 0;
                    }
                    return value;
                })
                .collect(Collectors.toList()));

        return value3;
    }

    public void convertBvhRotationOrderFromZXYToXYZ(Bvh target) {
        List<Joint> joints = target.getAllJointsAsList();

        joints.forEach(joint -> {
            if (Objects.nonNull(joint.getFrameData())) {
                joint.getFrameData().forEach(entry -> {
                    Rotation rotation = new Rotation(RotationOrder.XYZ,
                            RotationConvention.VECTOR_OPERATOR,
                            Math.toRadians(entry.getRotationX()),
                            Math.toRadians(entry.getRotationY()),
                            Math.toRadians(entry.getRotationZ()));
                    double[] angles;

                    try {
                        angles = rotation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
                    } catch (CardanEulerSingularityException e) {
                        rotation = new Rotation(RotationOrder.XYZ,
                                RotationConvention.VECTOR_OPERATOR,
                                Math.toRadians(entry.getRotationX() - 0.1d),
                                Math.toRadians(entry.getRotationY() - 0.1d),
                                Math.toRadians(entry.getRotationZ() - 0.1d));
                        angles = rotation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
                    }
                    entry.setRotationX((float) Math.toDegrees(angles[0]));
                    entry.setRotationY((float) Math.toDegrees(angles[1]));
                    entry.setRotationZ((float) Math.toDegrees(angles[2]));
                });
            }
        });
    }
}
