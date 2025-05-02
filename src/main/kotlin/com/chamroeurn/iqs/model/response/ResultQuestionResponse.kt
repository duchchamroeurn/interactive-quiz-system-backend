package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.AnswerEntity
import java.util.UUID

data class ResultQuestionResponse(
    val questionId: UUID,
    val questionText: String,
    val timeLimitInSecond: Int,
    val options: List<OptionResponse>,
    val answers: List<OptionResponse>
)

fun MutableList<AnswerEntity>.toResultQuestionResponse() = first().question.questionId?.let { questionId ->
    ResultQuestionResponse(
        questionId = questionId,
        questionText = first().question.questionText,
        timeLimitInSecond = first().question.timeLimitInSecond,
        options = first().question.options.mapNotNull { it.toResponse() },
        answers = mapNotNull { it.option.toResponse() }
    )
}
