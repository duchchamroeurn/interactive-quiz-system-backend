package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.CreateOptionRequest
import com.chamroeurn.iqs.model.request.UpdateOptionRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.repository.OptionRepository
import com.chamroeurn.iqs.repository.QuestionRepository
import com.chamroeurn.iqs.repository.entity.OptionEntity
import jakarta.persistence.Id
import org.springframework.data.domain.PageRequest
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

    fun updateOption(optionId: UUID, optionRequest: UpdateOptionRequest): SuccessResponse<OptionResponse> {

        val option = optionRepository.findById(optionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        option.optionText = optionRequest.optionText
        option.isCorrect = optionRequest.isCorrect
        val optionUpdated = optionRepository.save(option)

        return SuccessResponse(
            message = "Great! You have successfully updated the option.",
            data = optionUpdated.toResponse()
        )
    }

    fun allOptions(pageRequest: PageRequest): PagedResponse<OptionResponse> {

        val pageQuizzes = optionRepository.findAll(pageRequest)

        return PagedResponse(
            totalElements = pageQuizzes.totalElements,
            size = pageQuizzes.size,
            totalPages = pageQuizzes.totalPages,
            page = pageRequest.pageNumber,
            data = pageQuizzes.content.mapNotNull { it.toResponse() }
        )
    }

    fun viewOption(optionId: UUID): SuccessResponse<OptionDetailResponse> {

        val questionOption = optionRepository.findById(optionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        return SuccessResponse(
            data = questionOption.toOptionDetailResponse(),
            message = "We found the option you were looking for."
        )
    }

    fun deleteOption(optionId: UUID): SuccessResponse<String?> {
        val quiz = optionRepository.findById(optionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        optionRepository.delete(quiz)

        return SuccessResponse(
            message = "Great! You have successfully deleted the option.",
        )
    }

}