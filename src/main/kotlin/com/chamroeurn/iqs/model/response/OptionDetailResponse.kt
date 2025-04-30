package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.OptionEntity
import java.util.*

data class OptionDetailResponse(
    val id: UUID,
    val optionText: String,
    val isCorrect: Boolean,
    val question: QuestionResponse?
)

fun OptionEntity.toOptionDetailResponse() = optionId?.let {
    OptionDetailResponse(
        id = it,
        optionText = optionText,
        isCorrect = isCorrect,
        question = question.toResponse()
    )
}