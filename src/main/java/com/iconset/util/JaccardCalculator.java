package com.iconset.util;

import lombok.experimental.UtilityClass;

import java.util.*;

/**
 * Utility class for calculating Jaccard similarity between sets.
 * J(A, B) = |A ∩ B| / |A ∪ B|
 */
@UtilityClass
public class JaccardCalculator {

    /**
     * Calculate Jaccard similarity between two arrays.
     *
     * @param setA First set of icon IDs
     * @param setB Second set of icon IDs
     * @return Jaccard similarity [0.0, 1.0]
     */
    public static double calculate(Long[] setA, Long[] setB) {
        if (setA == null || setB == null || setA.length == 0 || setB.length == 0) {
            return 0.0;
        }

        Set<Long> a = new HashSet<>(Arrays.asList(setA));
        Set<Long> b = new HashSet<>(Arrays.asList(setB));

        // Calculate intersection
        Set<Long> intersection = new HashSet<>(a);
        intersection.retainAll(b);

        // Calculate union
        Set<Long> union = new HashSet<>(a);
        union.addAll(b);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    /**
     * Calculate Jaccard similarity for all pairwise combinations.
     *
     * @param sets Map of set index to icon IDs
     * @return Map of "setX_vs_setY" -> Jaccard similarity
     */
    public static Map<String, Double> calculatePairwise(Map<Integer, Long[]> sets) {
        Map<String, Double> results = new LinkedHashMap<>();
        List<Integer> indices = new ArrayList<>(sets.keySet());

        for (int i = 0; i < indices.size(); i++) {
            for (int j = i + 1; j < indices.size(); j++) {
                String key = String.format("set%d_vs_set%d", indices.get(i), indices.get(j));
                double jaccard = calculate(sets.get(indices.get(i)), sets.get(indices.get(j)));
                results.put(key, jaccard);
            }
        }

        return results;
    }

    /**
     * Calculate maximum Jaccard similarity across all pairs.
     */
    public static double calculateMax(Map<Integer, Long[]> sets) {
        return calculatePairwise(sets).values().stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElse(0.0);
    }

    /**
     * Calculate average Jaccard similarity across all pairs.
     */
    public static double calculateAverage(Map<Integer, Long[]> sets) {
        Map<String, Double> pairwise = calculatePairwise(sets);
        return pairwise.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
}
