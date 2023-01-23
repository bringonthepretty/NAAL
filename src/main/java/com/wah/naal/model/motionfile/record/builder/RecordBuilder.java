package com.wah.naal.model.motionfile.record.builder;

import com.wah.naal.exception.ParseException;
import com.wah.naal.model.motionfile.value.api.Value;
import com.wah.naal.model.motionfile.record.Record;
import com.wah.naal.model.motionfile.value.builder.api.ValueBuilder;
import com.wah.naal.model.motionfile.motion.Motion;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

/**
 * This class represents builder for {@link Motion}'s record
 */
public class RecordBuilder {

    private static RecordBuilder instance;

    private RecordBuilder() {}

    /**
     * Returns instance of {@link RecordBuilder}
     * @return instance of {@link RecordBuilder}
     */
    public static RecordBuilder getInstance() {
        if (Objects.isNull(instance)){
            instance = new RecordBuilder();
        }
        return instance;
    }

    /**
     * Builds {@link Record} from source bytes
     * @param sourceBytes source bytes
     * @param offset start of record's bytes
     * @return {@link Record} object
     */
    public Record build(List<Byte> sourceBytes, int offset){
        int boneIndex;
        int valueIndex;
        int recordType;
        int valueEntriesCount;
        int unknown;
        int union;
        Value value;
        if (offset < 0) {
            throw new ParseException("offset cannot be less than zero");
        }

        boneIndex = (int) ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 1), sourceBytes.get(offset + 0)}).getShort();
        valueIndex = (int) ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 2)}).get();
        recordType = (int) sourceBytes.get(offset + 3);
        valueEntriesCount = (int) ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 5), sourceBytes.get(offset + 4)}).getShort();
        unknown = (int) ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 7), sourceBytes.get(offset + 6)}).getShort();
        union = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 11), sourceBytes.get(offset + 10), sourceBytes.get(offset + 9), sourceBytes.get(offset + 8)}).getInt();

        if (!(recordType == 0) && union < 0){
            throw new ParseException("offset cannot be less than zero");
        }

        value = ValueBuilder.build(sourceBytes, offset, union, valueEntriesCount, recordType);
        return new Record(boneIndex, valueIndex, recordType, valueEntriesCount, unknown, union, value, offset);
    }
}
