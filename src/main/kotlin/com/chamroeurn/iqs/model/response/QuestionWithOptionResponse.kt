package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.QuestionEntity
import com.chamroeurn.iqs.repository.entity.QuestionTypes
import java.util.*

data class QuestionWithOptionResponse(
    val id: UUID,
    val questionText: String,
    val time: Int,
    val type: String,
    val isCustomize: Boolean,
    val correctAnswer: Boolean?,
    val options: List<OptionResponse>
)

fun QuestionEntity.toQuestionWithOptionResponse(): QuestionWithOptionResponse? {
    return questionId?.let { id ->
        if (isCustomize == false && type !== QuestionTypes.MULTIPLE_CHOICE) {
            if (type === QuestionTypes.YES_NO) {
                return QuestionWithOptionResponse(
                    id = id,
                    questionText = questionText,
                    time = timeLimitInSecond,
                    type = type.name,
                    isCustomize = false,
                    correctAnswer = correctAnswer,
                    options = mutableListOf(
                        OptionResponse(id = UUID.randomUUID(), optionText = "Yes", isCorrect = correctAnswer == true),
                        OptionResponse(id = UUID.randomUUID(), optionText = "No", isCorrect = correctAnswer == false),
                    )
                )
            } else {
                return QuestionWithOptionResponse(
                    id = id,
                    questionText = questionText,
                    time = timeLimitInSecond,
                    type = type.name,
                    isCustomize = false,
                    correctAnswer = correctAnswer,
                    options = mutableListOf(
                        OptionResponse(id = UUID.randomUUID(), optionText = "True", isCorrect = correctAnswer == true),
                        OptionResponse(
                            id = UUID.randomUUID(),
                            optionText = "False",
                            isCorrect = correctAnswer == false
                        ),
                    )
                )
            }
        }
        return QuestionWithOptionResponse(
            id = id,
            questionText = questionText,
            time = timeLimitInSecond,
            type = type.name,
            isCustomize = isCustomize == true,
            correctAnswer = correctAnswer,
            options = options.mapNotNull { it.toResponse() }
        )
    }
}