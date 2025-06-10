package com.chamroeurn.iqs.config.constants

import com.chamroeurn.iqs.model.response.OptionResponse
import com.chamroeurn.iqs.repository.entity.QuestionTypes
import java.util.UUID

object QuizConstants {

    // --- Static UUIDs for Fixed Options ---
    // You can generate these UUIDs once (e.g., using UUID.randomUUID() in a temporary main function
    // or an online UUID generator) and hardcode them here.
    // This ensures consistency across application restarts.

    private val YES_OPTION_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef") // Example UUID
    private val NO_OPTION_UUID = UUID.fromString("b2c3d4e5-f6a7-8901-2345-67890abcdeff")  // Example UUID
    private val TRUE_OPTION_UUID = UUID.fromString("c3d4e5f6-a7b8-9012-3456-7890abcdfe01") // Example UUID
    private val FALSE_OPTION_UUID = UUID.fromString("d4e5f6a7-b8c9-0123-4567-890abcfedcba") // Example UUID

    // --- Functions to get standard options with static IDs ---

    /**
     * Provides standard "Yes" and "No" options with static IDs.
     * The `isCorrect` status is set based on the `correctAnswer` parameter.
     *
     * @param correctAnswer Boolean indicating if 'Yes' is the correct answer.
     * @return A mutable list of OptionResponse objects.
     */
    fun getYesNoOptions(correctAnswer: Boolean): MutableList<OptionResponse> {
        return mutableListOf(
            OptionResponse(id = YES_OPTION_UUID, optionText = "Yes", isCorrect = correctAnswer),
            OptionResponse(id = NO_OPTION_UUID, optionText = "No", isCorrect = !correctAnswer)
        )
    }

    /**
     * Provides standard "True" and "False" options with static IDs.
     * The `isCorrect` status is set based on the `correctAnswer` parameter.
     *
     * @param correctAnswer Boolean indicating if 'True' is the correct answer.
     * @return A mutable list of OptionResponse objects.
     */
    fun getTrueFalseOptions(correctAnswer: Boolean): MutableList<OptionResponse> {
        return mutableListOf(
            OptionResponse(id = TRUE_OPTION_UUID, optionText = "True", isCorrect = correctAnswer),
            OptionResponse(id = FALSE_OPTION_UUID, optionText = "False", isCorrect = !correctAnswer)
        )
    }

    fun getAnswerBy(optionId: UUID?): Boolean {
       return mutableListOf(TRUE_OPTION_UUID, YES_OPTION_UUID).contains(optionId)
    }

    fun getAnswerOptionId(type: QuestionTypes, value: Boolean?): String? {
        return value?.let {
            return when (type) {
                QuestionTypes.MULTIPLE_CHOICE -> null
                QuestionTypes.TRUE_FALSE -> if (it) TRUE_OPTION_UUID.toString() else FALSE_OPTION_UUID.toString()
                QuestionTypes.YES_NO -> if (it) YES_OPTION_UUID.toString() else NO_OPTION_UUID.toString()
            }
        }
    }
}