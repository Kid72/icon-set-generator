package com.iconset.util;

import com.iconset.domain.GenerationRequest;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * AOP aspect for monitoring performance of icon set generation.
 * Records metrics and logs SLA violations.
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PerformanceMonitor {

    private final MeterRegistry meterRegistry;

    private static final long SLA_THRESHOLD_MS = 3000; // 3 seconds

    /**
     * Monitor performance of generateIconSets method.
     * Records execution time metrics and alerts on SLA violations.
     */
    @Around("execution(* com.iconset.service.IconSetGenerationService.generateIconSets(..))")
    public Object monitorGeneration(ProceedingJoinPoint pjp) throws Throwable {
        Instant start = Instant.now();
        Object[] args = pjp.getArgs();
        GenerationRequest request = (GenerationRequest) args[0];

        try {
            Object result = pjp.proceed();

            Duration elapsed = Duration.between(start, Instant.now());
            long elapsedMs = elapsed.toMillis();

            // Record metrics
            meterRegistry.timer("iconset.generation.time",
                Tags.of(
                    "numSets", String.valueOf(request.getNumSets()),
                    "itemsPerSet", String.valueOf(request.getItemsPerSet()),
                    "status", "success"
                )
            ).record(elapsed);

            // SLA monitoring
            if (elapsedMs > SLA_THRESHOLD_MS) {
                log.warn("SLA VIOLATION: Generation took {}ms for {} sets (threshold: {}ms)",
                         elapsedMs, request.getNumSets(), SLA_THRESHOLD_MS);

                meterRegistry.counter("iconset.generation.sla_violations",
                    Tags.of("numSets", String.valueOf(request.getNumSets()))
                ).increment();
            }

            // Log performance
            log.info("Generation performance: {}ms for {} sets of {} items",
                     elapsedMs, request.getNumSets(), request.getItemsPerSet());

            return result;

        } catch (Exception e) {
            Duration elapsed = Duration.between(start, Instant.now());

            // Record error metrics
            meterRegistry.counter("iconset.generation.errors",
                Tags.of(
                    "error", e.getClass().getSimpleName(),
                    "numSets", String.valueOf(request.getNumSets())
                )
            ).increment();

            meterRegistry.timer("iconset.generation.time",
                Tags.of(
                    "numSets", String.valueOf(request.getNumSets()),
                    "itemsPerSet", String.valueOf(request.getItemsPerSet()),
                    "status", "error"
                )
            ).record(elapsed);

            throw e;
        }
    }
}
