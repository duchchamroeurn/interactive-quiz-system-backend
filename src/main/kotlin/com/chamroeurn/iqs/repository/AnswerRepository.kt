package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.AnswerEntity
import com.chamroeurn.iqs.repository.entity.QuestionEntity
import com.chamroeurn.iqs.repository.entity.SessionEntity
import com.chamroeurn.iqs.repository.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AnswerRepository: JpaRepository<AnswerEntity, UUID> {
    fun getAnswersBySession(sessionId: SessionEntity): MutableList<AnswerEntity>
    fun getAnswersByUserAndSession(userId: UserEntity, sessionId: SessionEntity): MutableList<AnswerEntity>
    fun getAnswersByQuestionAndSession(questionId: QuestionEntity, sessionId: SessionEntity): MutableList<AnswerEntity>
    fun existsByUserAndSessionAndQuestion(userId: UserEntity, sessionId: SessionEntity, questionId: QuestionEntity): Boolean
}
