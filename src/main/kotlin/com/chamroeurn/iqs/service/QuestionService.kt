package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.CreateQuestionRequest
import com.chamroeurn.iqs.model.request.UpdateQuestionRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.repository.QuestionRepository
import com.chamroeurn.iqs.repository.QuizRepository
import com.chamroeurn.iqs.repository.entity.QuestionEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val quizRepository: QuizRepository,
) {

    fun createQuestion(createQuestionRequest: CreateQuestionRequest): SuccessResponse<QuestionResponse> {

        val quizId = parseId(createQuestionRequest.quizId)
        val quiz = quizRepository.findById(quizId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content.")
            val errors = mutableMapOf<String, String>()
            errors["quizId"] = "The quiz ID is not valid."
            problemDetail.properties = errors as Map<String, Any>?
            throw RestErrorResponseException(problemDetail)
        }

        val question = QuestionEntity(
            questionText = createQuestionRequest.questionText,
            timeLimitInSecond = createQuestionRequest.timeLimitInSecond,
            quiz = quiz
        )

        val questionSaved = questionRepository.save(question)

        return SuccessResponse(
            message = "Great! You have successfully created the question.",
            data = questionSaved.toResponse()
        )

    }

    fun viewQuestion(questionId: UUID): SuccessResponse<QuestionWithOptionResponse> {
        val question = questionRepository.findById(questionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        return SuccessResponse(
            data = question.toQuestionWithOptionResponse(),
            message = "We found the question you were looking for."
        )
    }

    fun allQuestions(pageRequest: PageRequest): PagedResponse<QuestionResponse> {

        val pageQuizzes = questionRepository.findAll(pageRequest)

        return PagedResponse(
            totalElements = pageQuizzes.totalElements,
            size = pageQuizzes.size,
            totalPages = pageQuizzes.totalPages,
            page = pageRequest.pageNumber,
            data = pageQuizzes.content.mapNotNull { it.toResponse() }
        )
    }

    fun deleteQuestion(questionId: UUID): SuccessResponse<String?> {
        val question = questionRepository.findById(questionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        questionRepository.delete(question)

        return SuccessResponse(
            message = "Great! You have successfully deleted the question.",
        )
    }

    fun updateQuestion(questionId: UUID, questionRequest: UpdateQuestionRequest): SuccessResponse<QuestionResponse> {

        val question = questionRepository.findById(questionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        question.questionText = questionRequest.questionText
        question.timeLimitInSecond = questionRequest.timeLimitInSecond

        val questionUpdated = questionRepository.save(question)

        return SuccessResponse(
            message = "Great! You have successfully updated the question.",
            data = questionUpdated.toResponse()
        )
    }

    private fun parseId(uuid: String): UUID {
        try {
            return UUID.fromString(uuid)
        } catch (e: Exception) {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content.")
            val errors = mutableMapOf<String, String>()
            errors["quizId"] = "The quiz ID is not valid."
            problemDetail.properties = errors as Map<String, Any>?
            throw RestErrorResponseException(problemDetail)
        }
    }
}