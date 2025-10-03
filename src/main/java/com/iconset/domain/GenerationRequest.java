package com.iconset.domain;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerationRequest {

    @Min(value = 1, message = "Number of sets must be at least 1")
    @Max(value = 1000, message = "Number of sets cannot exceed 1000")
    @Builder.Default
    private Integer numSets = 100;

    @Min(value = 1, message = "Items per set must be at least 1")
    @Max(value = 100, message = "Items per set cannot exceed 100")
    @Builder.Default
    private Integer itemsPerSet = 20;

    @DecimalMin(value = "0.0", message = "Overlap threshold must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Overlap threshold cannot exceed 1.0")
    @Builder.Default
    private Double overlapThreshold = 0.10;
}
