package com.jiangli.memorytravel.core;

import java.io.Serializable;

public class MemoryResult implements Serializable{
    int data;
    long startTime;
    long endTime;

    /**
     * @param data
     * @param startTime
     * @param endTime
     */
    public MemoryResult(int data, long startTime, long endTime) {
        super();
        this.data = data;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     *
     */
    public MemoryResult() {
        super();
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "MemoryResult{" +
                "data=" + data +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}