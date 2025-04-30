package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.CreateOptionRequest
import com.chamroeurn.iqs.model.response.OptionResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.model.response.toResponse
import com.chamroeurn.iqs.repository.OptionRepository
import com.chamroeurn.iqs.repository.QuestionRepository
import com.chamroeurn.iqs.repository.entity.OptionEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OptionService(
    private val questionRepository: QuestionRepository,
    private val optionRepository: OptionRepository
) {

    fun createOption(createOptionRequest: CreateOptionRequest): SuccessResponse<OptionResponse> {

        try {
            val questionId = UUID.fromString(createOptionRequest.questionId)
            val question = questionRepository.findById(questionId).orElseThrow { throw Exception() }

            val optionEntity = OptionEntity(
                question = question,
                optionText = createOptionRequest.optionText,
                isCorrect = createOptionRequest.isCorrect
            )

            val optionSaved = optionRepository.save(optionEntity)

            return SuccessResponse(
                data = optionSaved.toResponse(),
                message = "Great! You have successfully created the option."
            )

        } catch (ex: Exception) {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content.")
            val errors = mutableMapOf<String, String>()
            errors["quizId"] = "The quiz ID is not valid."
            problemDetail.properties = errors as Map<String, Any>?
            throw RestErrorResponseException(problemDetail)
        }
    }
}