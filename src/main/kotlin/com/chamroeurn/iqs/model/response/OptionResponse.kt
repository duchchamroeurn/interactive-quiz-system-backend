package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.OptionEntity
import java.util.UUID

data class OptionResponse(
    val id: UUID,
    val optionText: String,
    val isCorrect: Boolean
)


fun OptionEntity.toResponse() = optionId?.let {
    OptionResponse(
        id = it,
        optionText = optionText,
        isCorrect = isCorrect
    )
}