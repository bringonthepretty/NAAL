package com.wah.naal.model.motionfile.motion;

import com.wah.naal.utils.HexStringUtils;
import com.wah.naal.exception.ParseException;
import com.wah.naal.model.motionfile.record.Record;

import java.util.List;
import java.util.Objects;

/**
 * This class represents Nier:Automata proprietary motion format. header size 36 bytes<p>
 * Files with more than signed Integer.MAX_VALUE bytes (around 2 gigabytes) are not supported.
 * Therefore, all offsets greater that signed Integer.MAX_VALUE will throw {@link ParseException}.
 */
public final class Motion {
    private String fileDescription;
    private Long unknown1;
    private Integer flags;
    private Integer frameCount;
    private Integer recordsOffset;
    private Long recordsCount;
    private Long unknown2;
    private String motionName;
    private List<Record> records;

    /**
     * @param fileDescription offset 0x00, size 4 bytes, c type char[4], "mot\x00"
     * @param unknown1        offset 0x04, size 4 bytes, c type uint32_t
     * @param flags           offset 0x08, size 2 bytes, c type uint16_t
     * @param frameCount      offset 0x0A, size 2 bytes, c type int16_t
     * @param recordsOffset   offset 0x0C, size 4 bytes, c type uint32_t
     * @param recordsCount    offset 0x10, size 4 bytes, c type uint32_t
     * @param unknown2        offset 0x14, size 4 bytes, c type uint32_t
     * @param motionName      offset 0x18, size 12 bytes, c type char[12]
     */
    public Motion(String fileDescription, Long unknown1, Integer flags, Integer frameCount,
                  Integer recordsOffset, Long recordsCount, Long unknown2, String motionName,
                  List<Record> records) {
        this.fileDescription = fileDescription;
        this.unknown1 = unknown1;
        this.flags = flags;
        this.frameCount = frameCount;
        this.recordsOffset = recordsOffset;
        this.recordsCount = recordsCount;
        this.unknown2 = unknown2;
        this.motionName = motionName;
        this.records = records;
    }

    public Motion() {}

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public Long getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(Long unknown1) {
        this.unknown1 = unknown1;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public Integer getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(Integer frameCount) {
        this.frameCount = frameCount;
    }

    public Integer getRecordsOffset() {
        return recordsOffset;
    }

    public void setRecordsOffset(Integer recordsOffset) {
        this.recordsOffset = recordsOffset;
    }

    public Long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(Long recordsCount) {
        this.recordsCount = recordsCount;
    }

    public Long getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(Long unknown2) {
        this.unknown2 = unknown2;
    }

    public String getMotionName() {
        return motionName;
    }

    public void setMotionName(String motionName) {
        this.motionName = motionName;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String toStringAsHex() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Motion{")
                .append("\n")
                .append("fileDescription='")
                .append(fileDescription)
                .append('\'')
                .append("\n")
                .append("unknown1=")
                .append(HexStringUtils.toHexString(unknown1, 4))
                .append("\n")
                .append("flags=")
                .append(HexStringUtils.toHexString(flags, 2))
                .append("\n")
                .append("frameCount=")
                .append(HexStringUtils.toHexString(frameCount, 2))
                .append("\n")
                .append("recordsOffset=")
                .append(HexStringUtils.toHexString(recordsOffset, 4))
                .append("\n")
                .append("recordsCount=")
                .append(HexStringUtils.toHexString(recordsCount, 4))
                .append("\n")
                .append("unknown2=")
                .append(HexStringUtils.toHexString(unknown2, 4))
                .append("\n")
                .append("motionName='")
                .append(motionName)
                .append('\'')
                .append("\n")
                .append("records=")
                .append("\n");
        records.forEach((record) -> stringBuilder.append(record).append("\n"));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Motion{").append("\n")
                .append("fileDescription='").append(fileDescription).append('\'').append("\n")
                .append("unknown1=").append(unknown1).append("\n")
                .append("flags=").append(flags).append("\n")
                .append("frameCount=").append(frameCount).append("\n")
                .append("recordsOffset=").append(recordsOffset).append("\n")
                .append("recordsCount=").append(recordsCount).append("\n")
                .append("unknown2=").append(unknown2).append("\n")
                .append("motionName='").append(motionName).append('\'').append("\n")
                .append("records=").append("\n");

        records.forEach(record -> stringBuilder.append(record).append("\n"));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Motion) obj;
        return Objects.equals(this.fileDescription, that.fileDescription) &&
                Objects.equals(this.unknown1, that.unknown1) &&
                Objects.equals(this.flags, that.flags) &&
                Objects.equals(this.frameCount, that.frameCount) &&
                Objects.equals(this.recordsOffset, that.recordsOffset) &&
                Objects.equals(this.recordsCount, that.recordsCount) &&
                Objects.equals(this.unknown2, that.unknown2) &&
                Objects.equals(this.motionName, that.motionName) &&
                Objects.equals(this.records, that.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileDescription, unknown1, flags, frameCount, recordsOffset, recordsCount, unknown2, motionName, records);
    }

}
