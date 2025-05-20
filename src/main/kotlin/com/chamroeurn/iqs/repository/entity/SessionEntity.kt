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


)
