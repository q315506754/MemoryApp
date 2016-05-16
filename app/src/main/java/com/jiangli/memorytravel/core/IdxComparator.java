package com.jiangli.memorytravel.core;

import java.util.Comparator;

public class IdxComparator implements Comparator<MemoryAnalyseResult> {

    @Override
    public int compare(MemoryAnalyseResult o1, MemoryAnalyseResult o2) {
        return o1.getIdx() > o2.getIdx() ? 1 : -1;
    }


}
