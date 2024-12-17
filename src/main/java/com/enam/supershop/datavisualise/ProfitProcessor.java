package com.enam.supershop.datavisualise;

import java.util.*;
import java.time.LocalDateTime;

public class ProfitProcessor {

    public Map<Integer, Double> calculateProfitPerHour(List<ProfitDataFetcher.ProfitData> profitDataList) {
        Map<Integer, Double> hourlyProfitMap = new TreeMap<>();

        for (ProfitDataFetcher.ProfitData data : profitDataList) {
            int hour = data.getDateTime().getHour();
            hourlyProfitMap.put(hour, hourlyProfitMap.getOrDefault(hour, 0.0) + data.getAmountTaka());
        }

        return hourlyProfitMap;
    }
}

