package fr.ringularity.infiniteg.utils;

import java.util.*;

public final class Maps {
    public static <T> List<T> mapToIndexedList(Map<Integer, T> map) {
        if (map == null || map.isEmpty()) {
            return new ArrayList<>();
        }

        int maxIndex = -1;
        for (Integer k : map.keySet()) {
            if (k == null) continue;
            if (k < 0) throw new IllegalArgumentException("Negative index : " + k);
            if (k > maxIndex) maxIndex = k;
        }

        List<T> result = new ArrayList<>(Collections.nCopies(maxIndex + 1, (T) null));

        for (Map.Entry<Integer, T> e : map.entrySet()) {
            Integer idx = e.getKey();
            if (idx == null) continue;
            result.set(idx, e.getValue());
        }

        return result;
    }
}