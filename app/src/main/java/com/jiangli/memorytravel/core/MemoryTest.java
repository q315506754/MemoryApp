package com.jiangli.memorytravel.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MemoryTest {
    public static final String MODEL_RANDOM = "MODEL_RANDOM";
    public static final String MODEL_ALLANDDISORDER = "MODEL_ALLANDDISORDER";
    public static final String MODEL_ALLANDORDER = "MODEL_ALLANDORDER";
    public static final String MODEL_ALLANDREVERSE = "MODEL_ALLANDREVERSE";
    public static int repeat_times = 1;
    static Random random = new Random();

    public static boolean isEnd(String obj) {
        if (obj == null) {
            return true;
        }
        if ("end".equals(obj.trim())) {
            return true;
        }
        if (";".equals(obj.trim())) {
            return true;
        }
        if (".".equals(obj.trim())) {
            return true;
        }
        return false;
    }

    public static int random(int from, int to) {
        return from + random.nextInt(to - from + 1);
    }

    public static int[] generate(int from, int to, int GUESS_SIZE, String GUESS_MODEL) {
        // generate
        List<Integer> list = new LinkedList<Integer>();
        switch (GUESS_MODEL) {
            case MemoryTest.MODEL_RANDOM: {
                while (GUESS_SIZE-- > 0) {
                    list.add(random(from, to));
                }
                break;
            }
            case MemoryTest.MODEL_ALLANDDISORDER: {
                for (int i = 0; i < repeat_times; i++) {
                    for (int j = from; j <= to; j++) {
                        list.add(j);
                    }
                }
                Collections.shuffle(list, random);
                break;
            }
            case MemoryTest.MODEL_ALLANDORDER: {
                for (int i = 0; i < repeat_times; i++) {
                    for (int j = from; j <= to; j++) {
                        list.add(j);
                    }
                }
                break;
            }
            case MemoryTest.MODEL_ALLANDREVERSE: {
                for (int i = 0; i < repeat_times; i++) {
                    for (int j = to; j >= from; j--) {
                        list.add(j);
                    }
                }
                break;
            }
            default: {
//                System.err.println("default!");
            }
        }

        // switch
        int size = list.size();
        int[] ret = new int[size];
        for (int i = 0; i < size; i++) {
            ret[i] = list.get(i);
        }
        // System.arraycopy(list.toArray(new Integer[size]), 0, ret, 0, size);
        return ret;
    }


    /**
     * { "2": { "firstAppearedTime":30, "appearedTimes":10,
     * "appearedTime":[30,35.....,64], "costime":[1234,3333,....],
     * "totalCostTime":99231, "minimumCostTime":1234, "maximumCostTime":9999,
     * "averageCostTime":5555 } }
     */
    public static  List<MemoryAnalyseResult> analyse(List<MemoryResult> results) {
        Map<Integer, MemoryAnalyseResult> analyseResults = new HashMap<Integer, MemoryAnalyseResult>();

        int counter = 0;
        long totalTime = 0;
        for (MemoryResult rOne : results) {
            counter++;
            int key = rOne.getData();
            long costOfThisTime = rOne.getEndTime() - rOne.getStartTime();
            totalTime += costOfThisTime;

            MemoryAnalyseResult mData = analyseResults.get(key);

            if (mData == null) {
                mData = new MemoryAnalyseResult();
                mData.setData(key);
                mData.setFirstAppearedTime(counter);
                List<Integer> appearedTime = new ArrayList<Integer>();
                appearedTime.add(counter);
                mData.setAppearedTime(appearedTime);
                mData.setAppearedTimes(1);
                List<Long> costime = new ArrayList<Long>();
                costime.add(costOfThisTime);
                mData.setCostime(costime);
                mData.setTotalCostTime(costOfThisTime);
                mData.setMaximumCostTime(costOfThisTime);
                mData.setMinimumCostTime(costOfThisTime);
                mData.setAverageCostTime(costOfThisTime);

                analyseResults.put(key, mData);
            } else {
                List<Integer> appearedTime = mData.getAppearedTime();
                appearedTime.add(counter);
                mData.setAppearedTimes(mData.getAppearedTimes() + 1);
                List<Long> costime = mData.getCostime();
                costime.add(costOfThisTime);
                mData.setTotalCostTime(mData.getTotalCostTime() + costOfThisTime);

                // caculate average
                mData.setAverageCostTime(mData.getTotalCostTime() / mData.getAppearedTimes());

                // caculate min&max
                long calMin = costime.get(0);
                long calMax = costime.get(0);

                for (Long eachTime : costime) {
                    if (eachTime < calMin) {
                        calMin = eachTime;
                    }
                    if (eachTime > calMax) {
                        calMax = eachTime;
                    }
                }

                mData.setMinimumCostTime(calMin);
                mData.setMaximumCostTime(calMax);
            }

        }

        List<MemoryAnalyseResult> resultList = new ArrayList<MemoryAnalyseResult>();
        for (Integer key : analyseResults.keySet()) {
            MemoryAnalyseResult mOne = analyseResults.get(key);
            resultList.add(mOne);
        }
        Collections.sort(resultList, new MaxComparator());
        return resultList;
//        for (MemoryAnalyseResult mOne : resultList) {
//            String printStr = "max time:" + mOne.getData() + "->" + mOne.getMaximumCostTime();
//            if (mOne.getAppearedTimes() > 1) {
//                printStr = printStr + "(" + Arrays.toString(mOne.getCostime().toArray(new Long[mOne.getCostime().size()])) + " average:" + mOne.getAverageCostTime() + ")";
//            }
//        }
    }
}