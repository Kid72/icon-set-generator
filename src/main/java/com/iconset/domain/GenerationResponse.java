package com.iconset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerationResponse {

    private UUID requestId;
    private Integer totalSets;
    private Integer itemsPerSet;
    private Long executionTimeMs;
    private List<IconSetResult> sets;
    private Map<String, Object> statistics;
}
