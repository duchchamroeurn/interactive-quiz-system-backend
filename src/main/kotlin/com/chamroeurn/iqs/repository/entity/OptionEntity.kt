package com.chamroeurn.iqs.repository.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "tbl_options")
data class OptionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "option_id")
    val optionId: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    val question: QuestionEntity,

    @Column(name = "option_text", nullable = false)
    val optionText: String,

    @Column(name = "is_correct", nullable = false)
    val isCorrect: Boolean
)
