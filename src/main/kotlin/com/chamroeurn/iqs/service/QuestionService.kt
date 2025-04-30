package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.CreateQuestionRequest
import com.chamroeurn.iqs.model.response.QuestionResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.model.response.toResponse
import com.chamroeurn.iqs.repository.QuestionRepository
import com.chamroeurn.iqs.repository.QuizRepository
import com.chamroeurn.iqs.repository.entity.QuestionEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val quizRepository: QuizRepository
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

    private fun parseId(uuid: String): UUID {
        try {
            return UUID.fromString(uuid)
        } catch (e: Exception){
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content.")
            val errors = mutableMapOf<String, String>()
            errors["quizId"] = "The quiz ID is not valid."
            problemDetail.properties = errors as Map<String, Any>?
            throw RestErrorResponseException(problemDetail)
        }
    }
}