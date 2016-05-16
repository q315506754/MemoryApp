package com.jiangli.memorytravel.core;

import java.util.List;

public class MemoryAnalyseResult {
    // key
    int data;
    int idx;

    int firstAppearedTime;
    List<Integer> appearedTime;
    int appearedTimes;
    List<Long> costime;
    long totalCostTime;
    long minimumCostTime;
    long maximumCostTime;
    long averageCostTime;

    /**
     * @param data
     * @param firstAppearedTime
     * @param appearedTime
     * @param costime
     * @param totalCostTime
     * @param minimumCostTime
     * @param maximumCostTime
     * @param averageCostTime
     */
    public MemoryAnalyseResult(int data, int firstAppearedTime, List<Integer> appearedTime, List<Long> costime, long totalCostTime, long minimumCostTime, long maximumCostTime, long averageCostTime) {
        super();
        this.data = data;
        this.firstAppearedTime = firstAppearedTime;
        this.appearedTime = appearedTime;
        this.costime = costime;
        this.totalCostTime = totalCostTime;
        this.minimumCostTime = minimumCostTime;
        this.maximumCostTime = maximumCostTime;
        this.averageCostTime = averageCostTime;
    }


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    /**
     *
     */
    public MemoryAnalyseResult() {
        super();
    }

    public int getAppearedTimes() {
        return appearedTimes;
    }

    public void setAppearedTimes(int appearedTimes) {
        this.appearedTimes = appearedTimes;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getFirstAppearedTime() {
        return firstAppearedTime;
    }

    public void setFirstAppearedTime(int firstAppearedTime) {
        this.firstAppearedTime = firstAppearedTime;
    }

    public List<Integer> getAppearedTime() {
        return appearedTime;
    }

    public void setAppearedTime(List<Integer> appearedTime) {
        this.appearedTime = appearedTime;
    }

    public List<Long> getCostime() {
        return costime;
    }

    public void setCostime(List<Long> costime) {
        this.costime = costime;
    }

    public long getTotalCostTime() {
        return totalCostTime;
    }

    public void setTotalCostTime(long totalCostTime) {
        this.totalCostTime = totalCostTime;
    }

    public long getMinimumCostTime() {
        return minimumCostTime;
    }

    public void setMinimumCostTime(long minimumCostTime) {
        this.minimumCostTime = minimumCostTime;
    }

    public long getMaximumCostTime() {
        return maximumCostTime;
    }

    public void setMaximumCostTime(long maximumCostTime) {
        this.maximumCostTime = maximumCostTime;
    }

    public long getAverageCostTime() {
        return averageCostTime;
    }

    public void setAverageCostTime(long averageCostTime) {
        this.averageCostTime = averageCostTime;
    }

    @Override
    public String toString() {
        return "MemoryAnalyseResult [data(key)=" + data + ", firstAppearedTime=" + firstAppearedTime + ", appearedTime=" + appearedTime + ", appearedTimes=" + appearedTimes + ", costime=" + costime
                + ", totalCostTime=" + totalCostTime + ", minimumCostTime=" + minimumCostTime + ", maximumCostTime=" + maximumCostTime + ", averageCostTime=" + averageCostTime + "]";
    }


}