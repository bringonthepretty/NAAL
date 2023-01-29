package com.wah.naal.converter;

import com.wah.naal.io.FileIO;
import com.wah.naal.model.motionfile.motion.Motion;
import com.wah.naal.model.motionfile.record.Record;
import com.wah.naal.model.motionfile.value.api.Value;
import com.wah.naal.model.motionfile.value.impl.Value0;
import com.wah.naal.model.motionfile.value.impl.Value3;
import com.wah.naal.model.rawanimationfile.bone.Bone;
import com.wah.naal.model.rawanimationfile.framedata.RotationFrameData;
import com.wah.naal.model.rawanimationfile.rawanimation.RawAnimation;
import com.wah.naal.model.rawanimationfile.framedata.PositionFrameData;
import org.apache.commons.math3.geometry.euclidean.threed.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents converter from raw animation {@link RawAnimation} to Nier:Automata proprietary motion format {@link Motion}.
 * This class is singleton
 */
public class RawMotionToMotionConverter {

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

    private static final Map<Integer, Integer> bonesMap;

    static {
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

    private static RawMotionToMotionConverter instance;

    private RawMotionToMotionConverter() {}

    /**
     * Returns instance of {@link RawMotionToMotionConverter}
     * @return instance of {@link RawMotionToMotionConverter}
     */
    public static RawMotionToMotionConverter getInstance() {
        if (Objects.isNull(instance)) {
            instance = new RawMotionToMotionConverter();
        }
        return instance;
    }

    /**
     * Converts {@link RawAnimation} to {@link Motion}
     * @param source source raw animation
     * @return converted {@link Motion}
     */
    public Motion convert(RawAnimation source) {
        Motion result = new Motion();
        result.setRecords(getRecords(source));
        setMotionHeaderValues(result, source);
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

    private void setMotionHeaderValues(Motion target, RawAnimation source) {
        target.setFileDescription("mot");
        target.setUnknown1(DEFAULT_MOTION_HEADER_UNKNOWN1);
        target.setFlags(DEFAULT_MOTION_HEADER_FLAGS);
        target.setFrameCount(source.getBones().get(0).getFrameDataList().size());
        target.setRecordsCount((long)target.getRecords().size());
        target.setUnknown2(DEFAULT_MOTION_HEADER_UNKNOWN2);
        target.setMotionName(source.getName().replace("pl0100","pl0000")); //todo pl0100 is probably not only one possible string
    }

    private List<Record> getRecords(RawAnimation source) {
        List<Record> records = new ArrayList<>();
        List<Bone> bones = source.getBones();
        bones.forEach(joint -> records.addAll(convertBoneToRotationsRecordsList(joint)));

        records.addAll(convertPositionFrameDataListToPositionRecordList(source.getLocationFrameDataList()));

        records.addAll(getBaseBoneRotationDummy());

        return records;
    }

    private List<Record> convertBoneToRotationsRecordsList(Bone bone) {
        List<Record> result = new ArrayList<>();

        int boneIndex;

        try {
            boneIndex = Integer.parseInt(bone.getName().replace("bone", ""));
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

        recordRotationX.setValueEntriesCount(bone.getFrameDataList().size());
        recordRotationY.setValueEntriesCount(bone.getFrameDataList().size());
        recordRotationZ.setValueEntriesCount(bone.getFrameDataList().size());

        recordRotationX.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationY.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordRotationZ.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);

        List<Vector3D> rotationsAsCardan = new ArrayList<>();
        bone.getFrameDataList().forEach(rotationFrameData -> rotationsAsCardan.add(convertQuaternionRotationToCardanXYZRotation(rotationFrameData)));

        Value valueX = generateRotationValue(rotationsAsCardan.stream().map(vector3D -> (float)vector3D.getX()).collect(Collectors.toCollection(ArrayList::new)));
        Value valueY = generateRotationValue(rotationsAsCardan.stream().map(vector3D -> (float)vector3D.getY()).collect(Collectors.toCollection(ArrayList::new)));
        Value valueZ = generateRotationValue(rotationsAsCardan.stream().map(vector3D -> (float)vector3D.getZ()).collect(Collectors.toCollection(ArrayList::new)));

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

    private List<Record> convertPositionFrameDataListToPositionRecordList(List<PositionFrameData> positionFrameDataList) {
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

        recordPositionX.setValueEntriesCount(positionFrameDataList.size());
        recordPositionY.setValueEntriesCount(positionFrameDataList.size());
        recordPositionZ.setValueEntriesCount(positionFrameDataList.size());

        recordPositionX.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordPositionY.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);
        recordPositionZ.setUnknown(DEFAULT_RECORD_HEADER_UNKNOWN);

        List<Float> xData = positionFrameDataList.stream().map(PositionFrameData::getX).collect(Collectors.toCollection(ArrayList::new));
        List<Float> yData = positionFrameDataList.stream().map(PositionFrameData::getY).collect(Collectors.toCollection(ArrayList::new));
        List<Float> zData = positionFrameDataList.stream().map(PositionFrameData::getZ).collect(Collectors.toCollection(ArrayList::new));

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

    private Value generateRotationValue(List<Float> data) {
        return generateValue(data, 1d);
    }

    private Value generatePositionValue(List<Float> data) {
        return generateValue(data, 1d);
    }

    private Value generateValue(List<Float> data, Double ratio) {
        if (data.stream().allMatch(value -> value == 0f )) {
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
                    value = value % (VALUE_TYPE_3_MAX_NUMBER + 1); //todo is that necessary
                    if(value == Integer.MAX_VALUE) {
                        value = 0;
                    }
                    return value;
                })
                .collect(Collectors.toList()));

        return value3;
    }

    private Vector3D convertQuaternionRotationToCardanXYZRotation(RotationFrameData source) {
        Rotation rotation = new Rotation(source.getW(), source.getX(), source.getY(), source.getZ(), true);
        double[] angles;
        try {
            angles = rotation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
        } catch (CardanEulerSingularityException e) {
            rotation = new Rotation(source.getW(), source.getX() - 0.01, source.getY() - 0.01, source.getZ() - 0.01, true); //solution is highly questionable and better be replaced
            angles = rotation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
        }
        return new Vector3D(angles[0], angles[1], angles[2] * -1);
    }
}
