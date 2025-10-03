package com.iconset.repository;

import com.iconset.domain.IconSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IconSetRepository extends JpaRepository<IconSet, Long> {

    /**
     * Find all sets for a specific generation request, ordered by set index.
     */
    List<IconSet> findByRequestIdOrderBySetIndex(UUID requestId);

    /**
     * Find a specific set by request ID and set index.
     */
    Optional<IconSet> findByRequestIdAndSetIndex(UUID requestId, Integer setIndex);

    /**
     * Count distinct generation requests.
     */
    @Query("SELECT COUNT(DISTINCT is.requestId) FROM IconSet is")
    long countDistinctRequests();

    /**
     * Delete all sets for a specific request.
     */
    void deleteByRequestId(UUID requestId);
}
