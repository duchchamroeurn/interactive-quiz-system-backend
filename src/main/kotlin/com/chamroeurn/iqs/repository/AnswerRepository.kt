package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.model.response.SubmissionResponse
import com.chamroeurn.iqs.repository.entity.AnswerEntity
import com.chamroeurn.iqs.repository.entity.QuestionEntity
import com.chamroeurn.iqs.repository.entity.SessionEntity
import com.chamroeurn.iqs.repository.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AnswerRepository : JpaRepository<AnswerEntity, UUID> {
    fun getAnswersBySession(sessionId: SessionEntity): MutableList<AnswerEntity>
    fun getAnswersByUserAndSession(userId: UserEntity, sessionId: SessionEntity): MutableList<AnswerEntity>
    fun getAnswersByQuestionAndSession(questionId: QuestionEntity, sessionId: SessionEntity): MutableList<AnswerEntity>
    fun existsByUserAndSessionAndQuestion(
        userId: UserEntity,
        sessionId: SessionEntity,
        questionId: QuestionEntity
    ): Boolean

    fun getAnswersByUser(userId: UserEntity, pageable: Pageable): Page<AnswerEntity>
    @Query("""
        SELECT new com.chamroeurn.iqs.model.response.SubmissionResponse(
            s.sessionId,
            s.sessionCode,
            COUNT(DISTINCT q.questionId),
            SUM(
                CASE
                    WHEN q.type = 'MULTIPLE_CHOICE' THEN
                        CASE
                            WHEN opt.isCorrect = TRUE THEN 1L
                            ELSE 0L
                        END
                    WHEN q.type IN ('TRUE_FALSE', 'YES_NO') THEN
                        CASE
                            WHEN q.isCustomize = TRUE THEN
                                CASE
                                    WHEN opt.isCorrect = TRUE THEN 1L
                                    ELSE 0L
                                END
                            ELSE
                                CASE
                                    WHEN a.replyAnswer = q.correctAnswer THEN 1L
                                    ELSE 0L
                                END
                        END
                    ELSE 0L
                END
            ),
            qz.title,
            MAX(a.answerTime)
        )
        FROM AnswerEntity a
        LEFT JOIN a.session s
        LEFT JOIN s.quiz qz
        LEFT JOIN a.question q
        LEFT JOIN a.option opt
        WHERE a.user.userId = :userId
        GROUP BY s.sessionId, s.sessionCode, qz.title
        ORDER BY MAX(a.answerTime) DESC
        """
    )
    fun getUserSessionPerformance(
        @Param("userId") userId: UUID,
        pageable: Pageable
    ): Page<SubmissionResponse>
}
