package com.example.listing_service.modules.post.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post_view_histories")
public class PostViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "user_id")
    private Long userId; // Liên kết mềm, null nếu là khách vãng lai

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "viewed_at", nullable = false)
    private Instant viewedAt;

    @PrePersist
    public void prePersist() {
        if (this.viewedAt == null) {
            this.viewedAt = Instant.now();
        }
    }
}