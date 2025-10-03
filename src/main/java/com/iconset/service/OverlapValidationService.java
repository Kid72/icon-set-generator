package com.iconset.service;

import com.iconset.util.JaccardCalculator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for validating overlap constraints in generated icon sets.
 * Used primarily in development and testing to verify algorithm correctness.
 */
@Service
@Slf4j
public class OverlapValidationService {

    /**
     * Validate overlap constraint for all pairwise set comparisons.
     *
     * @param sets Map of setIndex -> iconIds
     * @param threshold Maximum allowed Jaccard similarity
     * @return Validation result with statistics and violations
     */
    public OverlapValidationResult validateOverlap(Map<Integer, Long[]> sets, double threshold) {
        log.debug("Validating overlap for {} sets with threshold {}", sets.size(), threshold);

        List<Integer> setIndices = new ArrayList<>(sets.keySet());
        int totalComparisons = 0;
        int violations = 0;
        double maxJaccard = 0.0;
        double sumJaccard = 0.0;
        List<OverlapViolation> violationList = new ArrayList<>();

        // Pairwise comparisons
        for (int i = 0; i < setIndices.size(); i++) {
            for (int j = i + 1; j < setIndices.size(); j++) {
                Long[] setA = sets.get(setIndices.get(i));
                Long[] setB = sets.get(setIndices.get(j));

                double jaccard = JaccardCalculator.calculate(setA, setB);

                totalComparisons++;
                sumJaccard += jaccard;
                maxJaccard = Math.max(maxJaccard, jaccard);

                if (jaccard > threshold) {
                    violations++;
                    violationList.add(new OverlapViolation(
                        setIndices.get(i),
                        setIndices.get(j),
                        jaccard
                    ));

                    log.warn("Overlap violation: sets [{}, {}] have Jaccard = {:.4f} > {}",
                             setIndices.get(i), setIndices.get(j), jaccard, threshold);
                }
            }
        }

        double avgJaccard = totalComparisons > 0 ? sumJaccard / totalComparisons : 0.0;

        OverlapValidationResult result = OverlapValidationResult.builder()
            .valid(violations == 0)
            .totalComparisons(totalComparisons)
            .violations(violations)
            .maxJaccard(maxJaccard)
            .avgJaccard(avgJaccard)
            .threshold(threshold)
            .violationDetails(violationList)
            .build();

        log.info("Overlap validation: valid={}, violations={}/{}, max={:.4f}, avg={:.4f}",
                 result.isValid(), violations, totalComparisons, maxJaccard, avgJaccard);

        return result;
    }

    /**
     * Validation result containing statistics and violations.
     */
    @Data
    @Builder
    public static class OverlapValidationResult {
        private boolean valid;
        private int totalComparisons;
        private int violations;
        private double maxJaccard;
        private double avgJaccard;
        private double threshold;
        private List<OverlapViolation> violationDetails;
    }

    /**
     * Details about a specific overlap violation.
     */
    @Data
    @AllArgsConstructor
    public static class OverlapViolation {
        private Integer setIndexA;
        private Integer setIndexB;
        private Double jaccardSimilarity;
    }
}
