package com.Jobs.JobsMemory.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "notifications"
)
public class Notification {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(
            name = "message",
            nullable = false,
            length = 500
    )
    private String message;

    @Column(nullable = false)
    private String type;

    @Column(
            name = "read",
            nullable = false
    )
    private boolean read = false;


    @Column(
            name = "created_at", nullable = false, updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "related_id")
    private Long relatedId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
