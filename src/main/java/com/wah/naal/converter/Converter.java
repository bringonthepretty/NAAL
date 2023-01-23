package com.wah.naal.converter;

import com.wah.naal.io.FileIO;
import com.wah.naal.model.bvhfile.bvh.Bvh;
import com.wah.naal.model.bvhfile.joint.Joint;
import com.wah.naal.model.motionfile.motion.Motion;
import com.wah.naal.model.motionfile.record.Record;
import com.wah.naal.model.motionfile.value.api.Value;
import com.wah.naal.model.motionfile.value.impl.Value0;
import com.wah.naal.model.motionfile.value.impl.Value3;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents converter from BioVision motion capture {@link Bvh} to
 * Nier:Automata proprietary motion format {@link Motion}.
 * This class is singleton
 */
public class Converter {

    private static final Double DEGREE_TO_AUTOMATA_UNITS_RATIO = 0.0174565566508981D;
    private static final Double DEGREE_360_TO_AUTOMATA_UNITS_RATIO = 6.284360394323324D;

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
        result.setRecords(getRecords(source));
        setMotionHeaderValues(result, source);
        setBaseBoneDummy(result);
        result.getRecords().sort(Comparator.comparingInt(Record::getBoneIndex));
        return result;
    }

    private void setBaseBoneDummy(Motion result) { //todo script ignores position for now
        Record recordPositionX = new Record();
        Record recordPositionY = new Record();
        Record recordPositionZ = new Record();
        Record recordRotationX = new Record();
        Record recordRotationY = new Record();
        Record recordRotationZ = new Record();

        recordPositionX.setBoneIndex(-1);
        recordPositionY.setBoneIndex(-1);
        recordPositionZ.setBoneIndex(-1);
        recordRotationX.setBoneIndex(-1);
        recordRotationY.setBoneIndex(-1);
        recordRotationZ.setBoneIndex(-1);

        recordPositionX.setValueIndex(0);
        recordPositionY.setValueIndex(1);
        recordPositionZ.setValueIndex(2);
        recordRotationX.setValueIndex(3);
        recordRotationY.setValueIndex(4);
        recordRotationZ.setValueIndex(5);

        recordPositionX.setRecordType(0);
        recordPositionY.setRecordType(0);
        recordPositionZ.setRecordType(0);
        recordRotationX.setRecordType(0);
        recordRotationY.setRecordType(0);
        recordRotationZ.setRecordType(0);

        recordPositionX.setValueEntriesCount(0);
        recordPositionY.setValueEntriesCount(0);
        recordPositionZ.setValueEntriesCount(0);
        recordRotationX.setValueEntriesCount(0);
        recordRotationY.setValueEntriesCount(0);
        recordRotationZ.setValueEntriesCount(0);

        recordPositionX.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordPositionY.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordPositionZ.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationX.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationY.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationZ.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);

        Value0 value0 = new Value0(0f);

        recordPositionX.setValue(value0);
        recordPositionY.setValue(value0);
        recordPositionZ.setValue(value0);
        recordRotationX.setValue(value0);
        recordRotationY.setValue(value0);
        recordRotationZ.setValue(value0);

        result.getRecords().add(recordPositionX);
        result.getRecords().add(recordPositionY);
        result.getRecords().add(recordPositionZ);
        result.getRecords().add(recordRotationX);
        result.getRecords().add(recordRotationY);
        result.getRecords().add(recordRotationZ);
    }

    private void setMotionHeaderValues(Motion target, Bvh source) {
        target.setFileDescription("mot");
        target.setUnknown1(DEFAULT_MOTION_HEADER_UNKNOWN1);
        target.setFlags(DEFAULT_MOTION_HEADER_FLAGS);
        target.setFrameCount(source.getFrameCount());
        target.setRecordsCount((long)target.getRecords().size());
        target.setUnknown2(DEFAULT_MOTION_HEADER_UNKNOWN2);
        target.setMotionName(source.getName().replace("pl0100","pl0000")); //todo pl0100 is probably not only one possible string
    }

    private List<Record> getRecords(Bvh source) {
        List<Record> records = new ArrayList<>();
        source.getAllJointsAsList().forEach(joint -> records.addAll(convertJointToRecordsList(joint)));
        return records;
    }

    private List<Record> convertJointToRecordsList(Joint joint) {
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

        Value valueX = setValueToRecord(getJointRotationAxisData(joint, 0));
        Value valueY = setValueToRecord(getJointRotationAxisData(joint, 1));
        Value valueZ = setValueToRecord(getJointRotationAxisData(joint, 2));

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

    private Value setValueToRecord(List<Float> data) { //todo implement clever value type choose
        Float maxDegreeNormalized;
        Float minDegree = data.stream().min(Float::compareTo).orElse(0f);
        Float valueDelta;
        Value3 value3 = new Value3();

        if (minDegree >= 0f) {
            data = data.stream().map(value -> value - minDegree).collect(Collectors.toCollection(ArrayList::new));
            value3.setValue((float) (minDegree * DEGREE_TO_AUTOMATA_UNITS_RATIO));
        } else {
            data = data.stream().map(value -> value + Math.abs(minDegree)).collect(Collectors.toCollection(ArrayList::new));
            value3.setValue((float) (minDegree * DEGREE_TO_AUTOMATA_UNITS_RATIO));
        }

        maxDegreeNormalized = data.stream().max(Float::compareTo).orElse(0f);
        valueDelta = (float) (maxDegreeNormalized * DEGREE_TO_AUTOMATA_UNITS_RATIO / 255);
        value3.setValueDelta(valueDelta);
        Float degreeToByteNumber = 255 / maxDegreeNormalized;

        value3.setEntries(data.stream()
                .map(frameDegree -> {
                    int value = (int)((frameDegree % 360) * degreeToByteNumber);
                    if(value == Integer.MAX_VALUE) {
                        value = 0;
                    }
                    return value;
                })
                .collect(Collectors.toList()));

        return value3;
    }
}
