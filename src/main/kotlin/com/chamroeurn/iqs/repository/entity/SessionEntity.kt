package com.chamroeurn.iqs.repository.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_sessions")
data class SessionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_id")
    val sessionId: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    val quiz: QuizEntity,

    @Column(name = "session_code", length = 10, unique = true, nullable = false)
    val sessionCode: String,

    @Column(name = "start_time")
    val startTime: LocalDateTime? = null,

    @Column(name = "end_time")
    var endTime: LocalDateTime? = null,

    @OneToMany(mappedBy = "session", cascade = [CascadeType.DETACH], orphanRemoval = true, fetch = FetchType.EAGER)
    val submittedAnswers: MutableList<AnswerEntity> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE]) // Cascade types for convenience
    @JoinTable(
        name = "tbl_session_users", // Name of the join table
        joinColumns = [JoinColumn(name = "session_id")], // Column for SessionEntity's ID in the join table
        inverseJoinColumns = [JoinColumn(name = "user_id")] // Column for UserEntity's ID in the join table
    )
    val accessibleUsers: MutableSet<UserEntity> = mutableSetOf(), // Users who can access this session

    // --- NEW FIELD FOR ACCESS CONTROL ---
    @Enumerated(EnumType.STRING) // Store enum as string in DB
    @Column(name = "access_type", nullable = false)
    var accessType: SessionAccessType = SessionAccessType.PRIVATE // Default to private
)

enum class SessionAccessType {
    PUBLIC,         // Available to everyone
    PRIVATE,        // Only accessible by specific assigned users (via accessibleUsers)
}
