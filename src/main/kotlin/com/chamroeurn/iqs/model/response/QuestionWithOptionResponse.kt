package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.QuestionEntity
import java.util.*

data class QuestionWithOptionResponse(
    val id: UUID,
    val questionText: String,
    val time: Int,
    val options: List<OptionResponse>
)

fun QuestionEntity.toQuestionWithOptionResponse() = questionId?.let { id ->
    QuestionWithOptionResponse(
        id = id,
        questionText = questionText,
        time = timeLimitInSecond,
        options = options.mapNotNull { it.toResponse() }
    )
}