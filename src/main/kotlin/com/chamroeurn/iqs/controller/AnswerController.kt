package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.SubmitAnswerRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.service.AnswerService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/v1/answer")
class AnswerController(
    private val answerService: AnswerService
) {

    @PostMapping("/submit")
    fun submitAnswer(@Valid @RequestBody body: SubmitAnswerRequest): ResponseEntity<SuccessResponse<AnswerResponse>> {
        val answerResponse = answerService.createAnswer(body)

        return ResponseEntity.ok(answerResponse)
    }

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
}