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
    val answerId: String
)

fun MutableList<AnswerEntity>.toSessionResultByUserResponse(): SessionResultByUserResponse? {
    val session = first().session
    val user = first().user
    val quiz = session.quiz
    val answers = mapNotNull {
        it.question.questionId?.let { it1 ->
            val answerValue = if (it.option == null) it.replyAnswer.toString() else it.option.optionId.toString()
                AnswerQuestionResponse(
                    questionId = it1,
                    answerId = answerValue
                )
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