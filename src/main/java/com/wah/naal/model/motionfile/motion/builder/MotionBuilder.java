package com.wah.naal.model.motionfile.motion.builder;

import com.wah.naal.exception.ParseException;
import com.wah.naal.model.motionfile.motion.Motion;
import com.wah.naal.model.motionfile.record.builder.RecordBuilder;
import com.wah.naal.model.motionfile.record.Record;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents builder for Nier:Automata proprietary motion format {@link Motion}
 */
public class MotionBuilder {

    private static MotionBuilder instance;

    private MotionBuilder() {}

    /**
     * Returns instance of {@link MotionBuilder}
     * @return instance of {@link MotionBuilder}
     */
    public static MotionBuilder getInstance() {
        if (Objects.isNull(instance)){
            instance = new MotionBuilder();
        }
        return instance;
    }

    /**
     * Builds {@link Motion} from source bytes
     * @param sourceBytes source bytes
     * @return {@link Motion} object
     */
    public Motion build(byte[] sourceBytes){
        List<Byte> list = new LinkedList<>();
        for (byte sourceByte : sourceBytes) {
            list.add(sourceByte);
        }
        return build(list);
    }

    /**
     * Builds {@link Motion} from source byte list
     * @param sourceBytes source byte list
     * @return {@link Motion} object
     */
    public Motion build(List<Byte> sourceBytes){
        String fileDescription;
        long unknown1;
        int flags;
        int frameCount;
        int recordsOffset;
        long recordsCount;
        long unknown2;
        String motionName;
        List<Record> records;

        fileDescription = String.valueOf((char) sourceBytes.get(0).byteValue()) +
                (char) sourceBytes.get(1).byteValue() +
                (char) sourceBytes.get(2).byteValue();

        unknown1 = Integer.toUnsignedLong(ByteBuffer.wrap(new byte[]{sourceBytes.get(7), sourceBytes.get(6), sourceBytes.get(5), sourceBytes.get(4)}).getInt());
        flags = Short.toUnsignedInt(ByteBuffer.wrap(new byte[]{sourceBytes.get(9), sourceBytes.get(8)}).getShort());
        frameCount = (int)ByteBuffer.wrap(new byte[]{sourceBytes.get(11), sourceBytes.get(10)}).getShort();
        recordsOffset = ByteBuffer.wrap(new byte[]{sourceBytes.get(15), sourceBytes.get(14), sourceBytes.get(13), sourceBytes.get(12)}).getInt();

        if (recordsOffset < 0) {
            throw new ParseException("File is too big");
        }

        recordsCount = Integer.toUnsignedLong(ByteBuffer.wrap(new byte[]{sourceBytes.get(19), sourceBytes.get(18), sourceBytes.get(17), sourceBytes.get(16)}).getInt());
        unknown2 = Integer.toUnsignedLong(ByteBuffer.wrap(new byte[]{sourceBytes.get(23), sourceBytes.get(22), sourceBytes.get(21), sourceBytes.get(20)}).getInt());

        StringBuilder motionNameBuilder = new StringBuilder();
        sourceBytes.subList(24, 35).forEach(aByte -> motionNameBuilder.append((char)aByte.byteValue()));
        motionName = motionNameBuilder.toString();

        records = initRecordsList(sourceBytes, recordsCount, recordsOffset);
        return new Motion(fileDescription, unknown1, flags, frameCount, recordsOffset, recordsCount, unknown2, motionName, records);
    }

    private List<Record> initRecordsList(List<Byte> sourceBytes, long recordsCount, int recordsOffset){
        List<Record> records = new ArrayList<>();
        for (int i = 0; i < recordsCount + 1; i++) {
            records.add(RecordBuilder.getInstance().build(sourceBytes, i * 12 + recordsOffset));
        }
        return records;
    }
}
