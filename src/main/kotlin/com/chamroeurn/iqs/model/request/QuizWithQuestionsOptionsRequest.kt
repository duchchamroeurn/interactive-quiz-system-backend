package com.chamroeurn.iqs.model.request

import com.chamroeurn.iqs.config.constants.AppConstants
import com.chamroeurn.iqs.repository.entity.QuestionTypes
import com.chamroeurn.iqs.validation.OptionsConsistentWithType
import com.chamroeurn.iqs.validation.ValueOfEnum
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.Instant

data class QuizWithQuestionsOptionsRequest(
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "The field quizId must be a valid UUID.")
    val id: String? = null,

    // TODO: Will remove while apply for spring security.
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "The field userId must be a valid UUID.")
    @field:NotBlank(message = "The field userId must not be blank.")
    val userId: String,

    @field:NotBlank(message = "Quiz title must not be blank.")
    @field:Size(min = 3, max = 255, message = "Quiz title must be between 3 and 255 characters long.")
    val title: String,

    @field:Size(max = 500, message = "The field quiz description must be in 500 characters length for maximum.")
    val description: String? = null,

    @field:NotEmpty(message = "Quiz must contain at least one question.")
    @field:Valid
    val questions: List<QuestionRequest>,

    val createdAt: Instant? = null
)

@OptionsConsistentWithType
data class QuestionRequest(
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "The field question id must be a valid UUID.")
    val id: String? = null,

    @field:NotBlank(message = "Question type must not be blank.")
    @field:ValueOfEnum(enumClass = QuestionTypes::class, message = "The provided question type is not valid.")
    val type: String,

    @field:NotBlank(message = "Question text must not be blank.")
    @field:Size(min = 5, max = 255, message = "Question text must be between 5 and 255 characters long.")
    val text: String,

    val correctAnswer: Boolean? = null,

    val isCustomize: Boolean? = null,

    @field:Positive(message = "Time limit must be greater than zero.")
    val timeLimit: Int,

    @field:Valid
    val options: List<OptionRequest>? = null
)

data class OptionRequest(
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "The field question id must be a valid UUID.")
    val id: String? = null,

    @field:NotNull(message = "Option text must not be blank.")
    val optionText: String,

    @field:NotNull(message = "The 'isCorrect' field for the option is required.")
    val correct: Boolean
)