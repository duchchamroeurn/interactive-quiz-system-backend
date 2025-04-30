package com.chamroeurn.iqs.repository.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "tbl_questions")
data class QuestionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "question_id")
    val questionId: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    val quiz: QuizEntity,

    @Column(name = "question_text", columnDefinition = "TEXT", nullable = false)
    var questionText: String,

    @Column(name = "time_limit_in_second", nullable = false)
    var timeLimitInSecond: Int,

    @OneToMany(mappedBy = "question", cascade = [CascadeType.DETACH], orphanRemoval = true, fetch = FetchType.EAGER)
    val options: MutableList<OptionEntity> = mutableListOf()
)
