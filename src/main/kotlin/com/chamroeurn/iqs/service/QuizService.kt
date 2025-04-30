package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.CreateQuizRequest
import com.chamroeurn.iqs.model.request.UpdateQuizRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.repository.QuizRepository
import com.chamroeurn.iqs.repository.UserRepository
import com.chamroeurn.iqs.repository.entity.QuizEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuizService(
    private val quizRepository: QuizRepository,
    private val userService: UserRepository
) {

    fun createQuiz(quizRequest: CreateQuizRequest): SuccessResponse<QuizResponse> {

        val presenterId = parseId(quizRequest.presenterId)
        val presenter = userService.findById(presenterId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content.")
            val errors = mutableMapOf<String, String>()
            errors["presenterId"] = "The presenter ID is not valid."
            problemDetail.properties = errors as Map<String, Any>?
            throw RestErrorResponseException(problemDetail)
        }

        val quiz = QuizEntity(title = quizRequest.title, presenter = presenter)

        val quizSaved = quizRepository.save(quiz)

        return SuccessResponse(
            message = "Great! You have successfully created the quiz.",
            data = quizSaved.toResponse()

        )
    }

    fun updateQuiz(quizId: UUID, quizRequest: UpdateQuizRequest): SuccessResponse<QuizResponse> {

        val quiz = quizRepository.findById(quizId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        quiz.title = quizRequest.title

        val quizUpdated = quizRepository.save(quiz)

        return SuccessResponse(
            message = "Great! You have successfully updated the quiz.",
            data = quizUpdated.toResponse()
        )
    }

    fun allQuizzes(pageRequest: PageRequest): PagedResponse<QuizResponse> {

        val pageQuizzes = quizRepository.findAll(pageRequest)

        return PagedResponse(
            totalElements = pageQuizzes.totalElements,
            size = pageQuizzes.size,
            totalPages = pageQuizzes.totalPages,
            page = pageRequest.pageNumber,
            data = pageQuizzes.content.mapNotNull { it.toResponse() }
        )
    }

    fun viewQuiz(quizId: UUID): SuccessResponse<QuizDetailResponse> {

        val quiz = quizRepository.findById(quizId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        return SuccessResponse(
            data = quiz.toQuizDetailResponse(),
            message = "We found the quiz you were looking for."
        )
    }

    fun deleteQuiz(quizId: UUID): SuccessResponse<String?> {
        val quiz = quizRepository.findById(quizId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        quizRepository.delete(quiz)

        return SuccessResponse(
            message = "Great! You have successfully deleted the quiz.",
        )
    }

    private fun parseId(uuid: String): UUID {
        try {
            return UUID.fromString(uuid)
        } catch (e: Exception) {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content.")
            val errors = mutableMapOf<String, String>()
            errors["presenterId"] = "The presenter ID is not valid."
            problemDetail.properties = errors as Map<String, Any>?
            throw RestErrorResponseException(problemDetail)
        }
    }
}