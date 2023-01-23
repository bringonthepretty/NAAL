package com.wah.naal.model.motionfile.record;

import com.wah.naal.model.motionfile.value.api.Value;

import java.util.Objects;

/**
 * This class represents Record. header size 12 bytes
 */
public class Record {
    private Integer boneIndex;
    private Integer valueIndex;
    private Integer recordType;
    private Integer valueEntriesCount;
    private Integer unknown;
    private Integer union;
    private Value value;
    private Integer offset;

    /**
     * @param boneIndex         offset 0x00, size 2 bytes, c type int16_t
     * @param valueIndex        offset 0x02, size 1 byte, c type char
     * @param recordType        offset 0x03, size 1 byte, c type char
     * @param valueEntriesCount offset 0x04, size 2 bytes, c type int16_t
     * @param unknown           offset 0x06, size 2 bytes, c type int16_t
     * @param union             offset 0x08, size 4 bytes, c type union float or uint32_t,
     *                          contains value for recordType 0x00 or offset for other recordType respectfully
     * @param value             implementation of this value depends on recordType field
     * @param offset            offset of record from file start
     */
    public Record(Integer boneIndex, Integer valueIndex, Integer recordType, Integer valueEntriesCount, Integer unknown, Integer union, Value value, Integer offset) {
        this.boneIndex = boneIndex;
        this.valueIndex = valueIndex;
        this.recordType = recordType;
        this.valueEntriesCount = valueEntriesCount;
        this.unknown = unknown;
        this.union = union;
        this.value = value;
        this.offset = offset;
    }

    public Record () {}

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Record{")
                .append("offset=").append(offset)
                .append(", boneIndex=").append(boneIndex)
                .append(", valueIndex=").append(valueIndex)
                .append(", recordType=").append(recordType)
                .append(", valueEntriesCount=").append(valueEntriesCount)
                .append(", unknown=").append(unknown)
                .append(", union=").append(union)
                .append(", value=").append(value)
                .append('}');

        return stringBuilder.toString();
    }

    public Integer getBoneIndex() {
        return boneIndex;
    }

    public void setBoneIndex(Integer boneIndex) {
        this.boneIndex = boneIndex;
    }

    public Integer getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(Integer valueIndex) {
        this.valueIndex = valueIndex;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getValueEntriesCount() {
        return valueEntriesCount;
    }

    public void setValueEntriesCount(Integer valueEntriesCount) {
        this.valueEntriesCount = valueEntriesCount;
    }

    public Integer getUnknown() {
        return unknown;
    }

    public void setUnknown(Integer unknown) {
        this.unknown = unknown;
    }

    public Integer getUnion() {
        return union;
    }

    public void setUnion(Integer union) {
        this.union = union;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Record) obj;
        return Objects.equals(this.boneIndex, that.boneIndex) &&
                Objects.equals(this.valueIndex, that.valueIndex) &&
                Objects.equals(this.recordType, that.recordType) &&
                Objects.equals(this.valueEntriesCount, that.valueEntriesCount) &&
                Objects.equals(this.unknown, that.unknown) &&
                Objects.equals(this.union, that.union) &&
                Objects.equals(this.value, that.value) &&
                Objects.equals(this.offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boneIndex, valueIndex, recordType, valueEntriesCount, unknown, union, value, offset);
    }

}
