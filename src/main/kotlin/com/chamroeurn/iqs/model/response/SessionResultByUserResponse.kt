package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.AnswerEntity
import java.util.UUID

data class SessionResultByUserResponse(
    val session: SessionResponse,
    val user: UserResponse,
    val quiz: QuizDetailResponse,
    val answers: List<AnswerQuestionResponse>
)

data class AnswerQuestionResponse(
    val questionId: UUID,
    val answerId: UUID
)

fun MutableList<AnswerEntity>.toSessionResultByUserResponse(): SessionResultByUserResponse? {
    val session = first().session
    val user = first().user
    val quiz = session.quiz
    val answers = mapNotNull {
        it.question.questionId?.let { it1 ->
            it.option?.optionId?.let { it2 ->
                AnswerQuestionResponse(
                    questionId = it1,
                    answerId = it2
                )
            }
        }
    }

    return session.toResponse()?.let {
        quiz.toQuizDetailResponse()
            ?.let { it1 ->
                SessionResultByUserResponse(
                    session = it,
                    user = user.toUserResponse(),
                    quiz = it1,
                    answers = answers
                )
            }
    }
}