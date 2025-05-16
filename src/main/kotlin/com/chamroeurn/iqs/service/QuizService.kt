package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.CreateQuizRequest
import com.chamroeurn.iqs.model.request.QuizWithQuestionsOptionsRequest
import com.chamroeurn.iqs.model.request.UpdateQuizRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.repository.OptionRepository
import com.chamroeurn.iqs.repository.QuestionRepository
import com.chamroeurn.iqs.repository.QuizRepository
import com.chamroeurn.iqs.repository.UserRepository
import com.chamroeurn.iqs.repository.entity.*
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class QuizService(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val optionRepository: OptionRepository,
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

    @Transactional
    fun createWithQuestions(quizRequest: QuizWithQuestionsOptionsRequest): SuccessResponse<QuizResponse> {

        /****
         * 1. Validate the user ID by check if exist user
         * 2. Validate the user role type it mean on role as presenter can create
         * ******/
        val presenterId = parseId(quizRequest.userId)
        val presenter = userService.findById(presenterId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content.")
            val errors = mutableMapOf<String, String>()
            errors["presenterId"] = "The presenter ID is not valid."
            problemDetail.properties = errors as Map<String, Any>?
            throw RestErrorResponseException(problemDetail)
        }
        //TODO: Will remove when apply for spring security.
        if (presenter.role !== UserRoles.PRESENTER) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "The user has no permission to access this feature."
            )
            throw RestErrorResponseException(problemDetail)
        }

        //Validation on Quiz
        //Check if quiz id exist for define whether user actions => create / update
        quizRequest.id?.let { it ->
            ///Update Quiz
            val id = parseId(it)
            val quizUpdate = quizRepository.findById(id)
        }

        //Create Quiz
        val quizCreate = QuizEntity(
            title = quizRequest.title,
            description = quizRequest.description,
            presenter = presenter
        )

        val quizSaved = quizRepository.save(quizCreate)

        //Section for questions

        quizRequest.questions.map { it ->
            val questionCreate = QuestionEntity(
                type = QuestionTypes.valueOf(it.type),
                questionText = it.text,
                timeLimitInSecond = it.timeLimit,
                quiz = quizSaved,
                correctAnswer = it.correctAnswer
            )
            val questionSaved = questionRepository.save(questionCreate)

            // Section for options
            it.options?.let {
                it.map { optionRequest ->
                    val optionCreate = OptionEntity(
                        question = questionSaved,
                        optionText = optionRequest.text,
                        isCorrect = optionRequest.isCorrect
                    )
                    optionRepository.save(optionCreate)
                }
            }

        }

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