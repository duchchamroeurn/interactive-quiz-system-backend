package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.UserSubmitAnswersRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.service.AnswerService
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1/answer")
class AnswerController(
    private val answerService: AnswerService
) {
    @GetMapping("/session/{sessionId}")
    fun viewAnswersBySessionId(
        @PathVariable sessionId: UUID,
    ): ResponseEntity<SuccessResponse<SessionResultResponse>> {

        val answersResponse = answerService.fetchAnswersBySession(sessionId)
        return ResponseEntity.ok(answersResponse)
    }

    @GetMapping("/session/{sessionId}/user/{userId}")
    fun viewAnswersBySessionIdUserId(
        @PathVariable sessionId: UUID,
        @PathVariable userId: UUID
    ): ResponseEntity<SuccessResponse<SessionResultByUserResponse>> {

        val answersResponse = answerService.fetchAnswersBySessionAndUser(sessionId, userId)
        return ResponseEntity.ok(answersResponse)
    }

    @GetMapping("/result")
    fun viewResultByQuestionInSession(
        @RequestParam sessionId: UUID,
        @RequestParam questionId: UUID
    ): ResponseEntity<SuccessResponse<ResultQuestionResponse>> {

        val resultQuestion = answerService.fetchResultQuestionInSession(sessionId, questionId)
        return ResponseEntity.ok(resultQuestion)
    }

    @PostMapping("/session/{sessionId}/user/{userId}")
    fun submitAnswersBySessionIdUserId(
        @PathVariable sessionId: UUID,
        @PathVariable userId: UUID,
        @Valid @RequestBody request: UserSubmitAnswersRequest
    ): ResponseEntity<SuccessResponse<String>> {
        val submitAnswerResponse = answerService.userSubmitAnswers(userId, sessionId, request)

        return ResponseEntity.ok(submitAnswerResponse)
    }

    @GetMapping("/user/{userId}")
    fun viewAnswersByUserId(
        @PathVariable userId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResponse<SubmissionResponse>> {
        val pageable = PageRequest.of(page, size)
        val answersResponse = answerService.fetchAnswersByUser(userId, pageable)
        return ResponseEntity.ok(answersResponse)
    }
}