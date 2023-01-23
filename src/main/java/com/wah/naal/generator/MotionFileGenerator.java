package com.wah.naal.generator;

import com.wah.naal.model.motionfile.motion.Motion;
import com.wah.naal.model.motionfile.record.Record;
import com.wah.naal.model.motionfile.value.api.Value;
import com.wah.naal.model.motionfile.value.impl.*;
import com.wah.naal.utils.FloatingPointConvertor;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents Nier:Automata proprietary motion format {@link Motion} writer.
 * This class is singleton
 */
public class MotionFileGenerator {

    private static final Integer DEFAULT_RECORDS_OFFSET = 44;

    private static final Integer UNION_SIZE = 8;

    private static final Byte TERMINATION_BYTE = (byte)0x00;

    private final FloatingPointConvertor floatingPointConvertor = FloatingPointConvertor.getInstance();

    private static MotionFileGenerator instance;

    private MotionFileGenerator() {}

    /**
     * Returns instance of {@link MotionFileGenerator}
     * @return instance of {@link MotionFileGenerator}
     */
    public static synchronized MotionFileGenerator getInstance() {
        if (Objects.isNull(instance)){
            instance = new MotionFileGenerator();
        }
        return instance;
    }

    /**
     * Returns C-type byte array representation of provided motion object. <p/>
     * All data except of offsets must be present in motion object.
     * If all necessary data is not provided, method behaviour is undefined
     * @param motion source motion
     * @return C-type byte array representation of provided motion object
     */
    public byte[] generateAsByteArray(Motion motion) {
        return ArrayUtils.toPrimitive(generate(motion).toArray(new Byte[0]));
    }

    /**
     * Returns byte list representation of provided motion object. <p/>
     * All data except of offsets must be present in motion object.
     * If all necessary data is not provided, method behaviour is undefined
     * @param motion source motion
     * @return byte list representation of provided motion object
     */
    public List<Byte> generate(Motion motion) {
        int approximateCapacity = (int) (1.1 *
                        (motion.getRecordsCount() < Integer.MAX_VALUE
                                ? motion.getRecordsCount().intValue()
                                : Integer.MAX_VALUE) *
                        motion.getRecords().stream()
                        .map(Record::getValueEntriesCount)
                        .max(Integer::compareTo)
                        .orElse(0));
        List<Byte> result = new ArrayList<>(approximateCapacity);
        writeHeader(result, motion);
        generateRecords(result, motion);
        writeValues(result, motion);
        return result;
    }

    private void writeHeader(List<Byte> target, Motion source) {
        appendString(target, source.getFileDescription());
        appendByte(target, TERMINATION_BYTE); //termination byte
        appendInteger(target, source.getUnknown1().intValue());
        appendShort(target, source.getFlags().shortValue());
        appendShort(target, source.getFrameCount().shortValue());
        appendInteger(target, DEFAULT_RECORDS_OFFSET);
        appendInteger(target, source.getRecords().size() - 1); //-1 is questionable and requires investigation
        appendInteger(target, source.getUnknown2().intValue());
        appendString(target, source.getMotionName());
        appendByte(target, TERMINATION_BYTE); //termination byte
        while (target.size() < DEFAULT_RECORDS_OFFSET) { //add 0s to match header size
            target.add((byte)0);
        }
    }

    private void generateRecords(List<Byte> target, Motion source) {
        source.getRecords().forEach(record -> {
            record.setOffset(target.size());
            appendShort(target, record.getBoneIndex().shortValue());
            appendByte(target, record.getValueIndex().byteValue());
            appendByte(target, record.getRecordType().byteValue());
            appendShort(target, record.getValueEntriesCount().shortValue());
            appendShort(target, record.getUnknown().shortValue());
            //allocate 4 bytes for union
            appendByte(target, (byte)0);
            appendByte(target, (byte)0);
            appendByte(target, (byte)0);
            appendByte(target, (byte)0);
        });
    }

    private void writeValues(List<Byte> target, Motion source) {
        source.getRecords().forEach(record -> {
            int unionOffset = record.getOffset() + UNION_SIZE;
            if (record.getRecordType() != 0){
                int unionValue = target.size() - record.getOffset();
                byte[] valueBytes = ByteBuffer.allocate(4)
                        .putInt(unionValue).array();
                writeValue(target, record.getValue());
                target.set(unionOffset + 0, valueBytes[3]);
                target.set(unionOffset + 1, valueBytes[2]);
                target.set(unionOffset + 2, valueBytes[1]);
                target.set(unionOffset + 3, valueBytes[0]);
            } else {
                Value0 value = (Value0) record.getValue();
                byte[] valueBytes = ByteBuffer.allocate(4)
                        .putFloat(value.getValue()).array();
                target.set(unionOffset + 0, valueBytes[3]);
                target.set(unionOffset + 1, valueBytes[2]);
                target.set(unionOffset + 2, valueBytes[1]);
                target.set(unionOffset + 3, valueBytes[0]);
            }
        });
    }

    private void writeValue(List<Byte> target, Value2 value) {
        append4BytesFloat(target, value.getValue());
        append4BytesFloat(target, value.getValueDelta());
        value.getEntries().forEach(integer -> appendShort(target, integer.shortValue()));
    }

    private void writeValue(List<Byte> target, Value3 value) {
        append2BytesFloat(target, value.getValue());
        append2BytesFloat(target, value.getValueDelta());
        value.getEntries().forEach(integer -> appendByte(target, integer.byteValue()));
        if (target.size() % 2 == 1){
            appendByte(target, (byte)0); //is this needed? added to make all offsets even
        }
    }

    private void writeValue(List<Byte> target, Value5 value) {
        append4BytesFloat(target, value.getValue());
        append4BytesFloat(target, value.getValueDelta());
        append4BytesFloat(target, value.getIncomingDerivativeValue());
        append4BytesFloat(target, value.getIncomingDerivativeValueDelta());
        append4BytesFloat(target, value.getOutgoingDerivativeValue());
        append4BytesFloat(target, value.getOutgoingDerivativeValueDelta());
        value.getEntries().forEach(entry -> {
            appendShort(target, entry.absoluteFrameIndex().shortValue());
            appendShort(target, entry.valueQuantum().shortValue());
            appendShort(target, entry.incomingDerivativeQuantum().shortValue());
            appendShort(target, entry.outgoingDerivativeQuantum().shortValue());
        });
    }

    private void writeValue(List<Byte> target, Value6 value) {
        append2BytesFloat(target, value.getValue());
        append2BytesFloat(target, value.getValueDelta());
        append2BytesFloat(target, value.getIncomingDerivativeValue());
        append2BytesFloat(target, value.getIncomingDerivativeValueDelta());
        append2BytesFloat(target, value.getOutgoingDerivativeValue());
        append2BytesFloat(target, value.getOutgoingDerivativeValueDelta());
        value.getEntries().forEach(entry -> {
            appendByte(target, entry.absoluteFrameIndex().byteValue());
            appendByte(target, entry.valueQuantum().byteValue());
            appendByte(target, entry.incomingDerivativeQuantum().byteValue());
            appendByte(target, entry.outgoingDerivativeQuantum().byteValue());
        });
    }

    private void writeValue(List<Byte> target, Value value) {
        switch (value.getType()) {
            case 1, 7, 8, 4 -> throw new UnsupportedOperationException("Record type " + value.getType() + " is not supported");
            case 2 -> writeValue(target, (Value2) value);
            case 3 -> writeValue(target, (Value3) value);
            case 5 -> writeValue(target, (Value5) value);
            case 6 -> writeValue(target, (Value6) value);
            default -> throw new IllegalArgumentException("Unknown record type " + value.getType());
        }
    }

    private void append4BytesFloat(List<Byte> target, float value){
        byte[] valueBytes = ByteBuffer.allocate(4).putFloat(value).array();
        ArrayUtils.reverse(valueBytes);
        for (byte b : valueBytes) {
            target.add(b);
        }
    }

    private void append2BytesFloat(List<Byte> target, float value) {
        byte[] valueBytes = floatingPointConvertor.floatToBytes(value);
        ArrayUtils.reverse(valueBytes);
        for (byte b : valueBytes) {
            target.add(b);
        }
    }

    private void appendInteger(List<Byte> target, int value) {
        byte[] valueBytes = ByteBuffer.allocate(4)
                .putInt(value).array();
        ArrayUtils.reverse(valueBytes);
        for (byte b: valueBytes){
            target.add(b);
        }
    }

    private void appendShort(List<Byte> target, short value) {
        byte[] valueBytes = ByteBuffer.allocate(2)
                .putShort(value).array();
        ArrayUtils.reverse(valueBytes);
        for (byte b : valueBytes) {
            target.add(b);
        }
    }

    private void appendByte(List<Byte> target, byte value) {
        target.add(value);
    }

    private void appendString(List<Byte> target, String value) {
        byte[] valueBytes = value.getBytes();
        for (byte b: valueBytes){
            target.add(b);
        }
    }
}
