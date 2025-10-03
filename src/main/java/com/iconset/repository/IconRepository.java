package com.iconset.repository;

import com.iconset.domain.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {

    /**
     * Call the HPSS algorithm function to generate icon sets.
     * Returns raw Object[] with [set_id, icon_ids[]]
     */
    @Query(value = """
        SELECT set_id, icon_ids
        FROM generate_icon_sets_optimized(:numSets, :itemsPerSet, CAST(:threshold AS DECIMAL))
        """, nativeQuery = true)
    List<Object[]> generateIconSetsNative(
        @Param("numSets") int numSets,
        @Param("itemsPerSet") int itemsPerSet,
        @Param("threshold") double threshold
    );

    /**
     * Validate if generation is feasible with current icon pool.
     * Returns JSONB as string with feasibility info.
     */
    @Query(value = """
        SELECT CAST(validate_generation_feasibility(
            :numSets,
            :itemsPerSet,
            CAST(:threshold AS DECIMAL)
        ) AS text)
        """, nativeQuery = true)
    String validateFeasibility(
        @Param("numSets") int numSets,
        @Param("itemsPerSet") int itemsPerSet,
        @Param("threshold") double threshold
    );

    /**
     * Find icons by category (for filtering support).
     */
    List<Icon> findByCategory(String category);

    /**
     * Count icons by category.
     */
    @Query(value = """
        SELECT COUNT(*) FROM icons WHERE category = :category
        """, nativeQuery = true)
    long countByCategory(@Param("category") String category);
}
