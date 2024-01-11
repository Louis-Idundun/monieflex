package com.sq018.monieflex.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeUtils {
    public static List<String> getMonths() {
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime model = current.minusMonths(8);
        List<String> months = new ArrayList<>();

        while(model.isBefore(current)) {
            months.add(current.getMonth().name());
            current = current.minusMonths(1);
        }
        Collections.reverse(months);
        return months;
    }
}
