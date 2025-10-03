package com.iconset.controller;

import com.iconset.domain.GenerationRequest;
import com.iconset.domain.GenerationResponse;
import com.iconset.domain.IconSet;
import com.iconset.repository.IconSetRepository;
import com.iconset.service.IconSetGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST API controller for icon set generation using HPSS algorithm.
 */
@RestController
@RequestMapping("/api/v1/icon-sets")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Icon Set Generation", description = "HPSS Algorithm for generating icon sets with overlap constraints")
public class IconSetController {

    private final IconSetGenerationService generationService;
    private final IconSetRepository iconSetRepository;

    /**
     * Generate N icon sets with overlap constraint.
     *
     * @param request Generation parameters
     * @return Generation response with all sets
     */
    @PostMapping("/generate")
    @Operation(summary = "Generate icon sets",
               description = "Generate N icon sets of M icons each with Jaccard similarity ≤ threshold using HPSS algorithm")
    public ResponseEntity<GenerationResponse> generateSets(@RequestBody @Valid GenerationRequest request) {
        log.info("Received generation request: {}", request);

        GenerationResponse response = generationService.generateIconSets(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all sets for a generation request.
     *
     * @param requestId UUID of the generation request
     * @return List of icon sets
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<List<IconSet>> getIconSets(@PathVariable UUID requestId) {
        List<IconSet> sets = generationService.getIconSets(requestId);

        if (sets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sets);
    }

    /**
     * Get a specific icon set.
     *
     * @param requestId UUID of the generation request
     * @param setIndex Index of the set
     * @return Icon set
     */
    @GetMapping("/{requestId}/{setIndex}")
    public ResponseEntity<IconSet> getIconSet(
            @PathVariable UUID requestId,
            @PathVariable Integer setIndex) {

        return generationService.getIconSet(requestId, setIndex)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete all sets for a request.
     *
     * @param requestId UUID of the generation request
     * @return No content
     */
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteIconSets(@PathVariable UUID requestId) {
        generationService.deleteIconSets(requestId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check endpoint.
     *
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", Instant.now(),
            "totalRequests", iconSetRepository.countDistinctRequests()
        ));
    }
}
