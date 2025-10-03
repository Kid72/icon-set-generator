package com.iconset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IconSetResult {

    private Integer setIndex;
    private Long[] iconIds;
    private Map<String, Object> metadata;
}
