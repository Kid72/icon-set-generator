package com.iconset.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "icon_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IconSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id", nullable = false)
    private UUID requestId;

    @Column(name = "set_index", nullable = false)
    private Integer setIndex;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "icon_ids", columnDefinition = "bigint[]", nullable = false)
    private Long[] iconIds;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
