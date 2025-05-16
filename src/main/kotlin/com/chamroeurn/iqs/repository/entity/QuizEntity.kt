package com.chamroeurn.iqs.repository.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_quizzes")

data class QuizEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "quiz_id")
    val quizId: UUID? = null,

    @Column(name = "quiz_title", nullable = false)
    var title: String,

    @Column(name = "quiz_des", nullable = true, length = 500)
    var description: String? = null,

    @ManyToOne
    @JoinColumn(name = "presenter_id", nullable = false)
    val presenter: UserEntity,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "quiz", cascade = [CascadeType.DETACH], orphanRemoval = true, fetch = FetchType.EAGER)
    val questions: MutableList<QuestionEntity> = mutableListOf(),

    @OneToMany(mappedBy = "quiz", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    val sessions: MutableList<SessionEntity> = mutableListOf()
)
