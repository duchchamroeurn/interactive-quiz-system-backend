package com.chamroeurn.iqs.repository.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_answers")
data class AnswerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "answer_id")
    val answerId: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    val session: SessionEntity,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    val question: QuestionEntity,

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = true)
    val option: OptionEntity? = null,

    @Column(name = "reply_answer", nullable = true)
    val replyAnswer: Boolean? = null,

    @Column(name = "answer_time")
    val answerTime: LocalDateTime? = LocalDateTime.now()
)
