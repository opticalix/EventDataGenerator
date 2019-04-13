package com.opticalix.common.pojo;

import java.io.Serializable;

/**
 * @author Felix
 * @date 06/01/2019 7:53 PM
 * @email opticalix@gmail.com
 */
public class SmartHomeRawEvent implements Serializable {
    private long id;
    /**
     * sec
     */
    private long timestamp;
    private float value;
    /**
     * 0: work, 1: load
     */
    private byte property;
    private int plugId;
    private int householdId;
    private int houseId;

    public SmartHomeRawEvent() {
    }

    public SmartHomeRawEvent(long id, long timestamp, float value, byte property, int plugId, int householdId, int houseId) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
        this.property = property;
        this.plugId = plugId;
        this.householdId = householdId;
        this.houseId = houseId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public byte getProperty() {
        return property;
    }

    public void setProperty(byte property) {
        this.property = property;
    }

    public int getPlugId() {
        return plugId;
    }

    public void setPlugId(int plugId) {
        this.plugId = plugId;
    }

    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    @Override
    public String toString() {
        return "SmartHomeRawEvent{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", value=" + value +
                ", property=" + property +
                ", plugId=" + plugId +
                ", householdId=" + householdId +
                ", houseId=" + houseId +
                '}';
    }

    public static SmartHomeRawEvent convert2SmartHomeEvent(String rawLine) {
        String comma = ",";
        String[] split = rawLine.split(comma);
        long id = Long.parseLong(split[0]);
        long timestamp = Long.parseLong(split[1]);
        float value = Float.parseFloat(split[2]);
        byte property = Byte.parseByte(split[3]);
        int plugId = Integer.parseInt(split[4]);
        int householdId = Integer.parseInt(split[5]);
        int houseId = Integer.parseInt(split[6]);
        return new SmartHomeRawEvent(id, timestamp, value, property, plugId, householdId, houseId);
    }
}
