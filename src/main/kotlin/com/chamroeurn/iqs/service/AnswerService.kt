package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.UserSubmitAnswersRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.repository.*
import com.chamroeurn.iqs.repository.entity.*
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val optionRepository: OptionRepository,
    private val questionRepository: QuestionRepository
) {

    fun fetchAnswersBySession(sessionId: UUID): SuccessResponse<SessionResultResponse> {
        val session = sessionRepository.findById(sessionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        val answers = answerRepository.getAnswersBySession(session)
        if (answers.isEmpty()) {
            return SuccessResponse(
                data = session.sessionId?.let {
                    SessionResultResponse(
                        sessionId = it,
                        sessionCode = session.sessionCode,
                        startTime = session.startTime,
                        endTime = session.endTime,
                        participants = emptyList()
                    )
                },
                message = "Successful list answers by session"
            )
        }
        return SuccessResponse(
            data = answers.toSessionResultResponse(),
            message = "Successful list answers by session"
        )

    }

    fun fetchAnswersBySessionAndUser(sessionId: UUID, userId: UUID): SuccessResponse<SessionResultByUserResponse> {
        val session = sessionRepository.findById(sessionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }
        val user = userRepository.findById(userId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        val answers = answerRepository.getAnswersByUserAndSession(user, session)
        if (answers.isEmpty()) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        return SuccessResponse(
            message = "Successful list answers by user in the session.",
            data = answers.toSessionResultByUserResponse()
        )
    }

    fun fetchResultQuestionInSession(sessionId: UUID, questionId: UUID): SuccessResponse<ResultQuestionResponse> {
        val session = sessionRepository.findById(sessionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        val question = questionRepository.findById(questionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        val answers = answerRepository.getAnswersByQuestionAndSession(question, session)

        return SuccessResponse(
            data = answers.toResultQuestionResponse(),
            message = "Successful list answers by question in the session."
        )
    }

    @Transactional
    fun userSubmitAnswers(userId: UUID, sessionId: UUID, request: UserSubmitAnswersRequest): SuccessResponse<String> {

        val user = userRepository.findById(userId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        if (user.role == UserRoles.ADMIN) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "Access to the requested resource is forbidden due to insufficient user privileges."
            )
            throw RestErrorResponseException(problemDetail)
        }

        val session = sessionRepository.findById(sessionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        if (isSessionEnded(session)) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "This session has ended. Submissions are no longer allowed."
            )
            throw RestErrorResponseException(problemDetail)
        }

        if (isQuizOwner(user, session.quiz)) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "Quiz owners are not allowed to submit answers to their own quizzes."
            )
            throw RestErrorResponseException(problemDetail)
        }

        val answerEntities = request.answers.map { answer ->
            val questionId = answer.questionId
            val question = questionRepository.findById(questionId).orElseThrow {
                val problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND,
                    "The content you are trying to access does not exist."
                )
                throw RestErrorResponseException(problemDetail)
            }

            val hasUserSubmittedAnswerForSession =
                answerRepository.existsByUserAndSessionAndQuestion(user, session, question)
            if (hasUserSubmittedAnswerForSession) {
                val problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "You have already submitted answers for this session."
                )
                throw RestErrorResponseException(problemDetail)
            }

            if (!isQuestionBelongsToQuiz(question, session.quiz)) {
                val problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "The submitted question does not belong to the quiz of this session."
                )
                throw RestErrorResponseException(problemDetail)
            }
            var option: OptionEntity? = null
            var answersBoolean: Boolean? = null
            try {
                val optionId = UUID.fromString(answer.submittedValue)
                if (optionId !== null) {
                    option = optionRepository.findById(optionId).orElseThrow {
                        val problemDetail = ProblemDetail.forStatusAndDetail(
                            HttpStatus.NOT_FOUND,
                            "The content you are trying to access does not exist."
                        )
                        throw RestErrorResponseException(problemDetail)
                    }
                    if (!isOptionBelongsToQuestion(option, question)) {

                        val problemDetail = ProblemDetail.forStatusAndDetail(
                            HttpStatus.BAD_REQUEST,
                            "The selected option does not belong to the submitted question."
                        )
                        throw RestErrorResponseException(problemDetail)
                    }
                }
            } catch (ex: Exception) {
                answersBoolean = answer.submittedValue.lowercase() === "true"
            }

           AnswerEntity(
                user = user,
                session = session,
                question = question,
                option = option,
                replyAnswer = answersBoolean
            )

        }

        answerRepository.saveAll(answerEntities)

        return SuccessResponse(
            message = "You have successful submitted the answers.",
            data = "Success"
        )
    }

    private fun isOptionBelongsToQuestion(option: OptionEntity, question: QuestionEntity): Boolean {
        return option.question.questionId == question.questionId
    }

    private fun isSessionEnded(session: SessionEntity): Boolean {
        val endTime = session.endTime
        return endTime != null && LocalDateTime.now().isAfter(endTime)
    }

    private fun isQuizOwner(user: UserEntity, quiz: QuizEntity): Boolean {
        return user.userId == quiz.presenter.userId
    }

    private fun isQuestionBelongsToQuiz(question: QuestionEntity, quiz: QuizEntity): Boolean {
        return question.quiz.quizId == quiz.quizId
    }
}