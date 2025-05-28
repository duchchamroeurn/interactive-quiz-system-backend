package com.chamroeurn.iqs.model.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class SubmittedAnswer(
    @field:NotNull(message = "Question ID cannot be null")
    val questionId: UUID, // Assuming question IDs are UUIDs

    @field:NotNull(message = "Answer value cannot be null")
    @field:Size(max = 2000, message = "Answer value must not exceed 2000 characters")
    // If answers can be of various types (e.g., String, Int, List<String>),
    // you might keep it as Any, but then validate its actual type/content in the service layer.
    // For now, let's assume it's a simple string for most cases.
    val submittedValue: String // Or Any, if truly dynamic
)
data class UserSubmitAnswersRequest(
    @field:Valid
    var answers: List<SubmittedAnswer>
)

