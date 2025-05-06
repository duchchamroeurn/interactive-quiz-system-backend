package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.service.SessionService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/v1/session")
class SessionController(
    private val sessionService: SessionService
) {

    @PostMapping("/start/{quizId}")
    fun startSession(@PathVariable quizId: UUID): ResponseEntity<SuccessResponse<SessionResponse>> {

        val sessionResponse = sessionService.createNewSession(quizId)
        return ResponseEntity.ok(sessionResponse)
    }

    @GetMapping("/code/{sessionCode}")
    fun getSessionByCode(@PathVariable sessionCode: String): ResponseEntity<SuccessResponse<SessionDetailResponse>> {

        val sessionDetailResponse = sessionService.fetchBySessionCode(sessionCode)
        return ResponseEntity.ok(sessionDetailResponse)
    }

    @GetMapping("/{sessionId}")
    fun getSessionById(@PathVariable sessionId: UUID): ResponseEntity<SuccessResponse<SessionDetailResponse>> {

        val sessionDetailResponse = sessionService.fetchBySessionId(sessionId)
        return ResponseEntity.ok(sessionDetailResponse)
    }

    @PostMapping("/end/{sessionId}")
    fun finishSession(@PathVariable sessionId: UUID): ResponseEntity<SuccessResponse<SessionResponse>> {

        val sessionDetailResponse = sessionService.endSession(sessionId)
        return ResponseEntity.ok(sessionDetailResponse)
    }

    @GetMapping
    fun listSessions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResponse<SessionResponse>> {

        val pageQuizResponse = sessionService.allSessions(PageRequest.of(page, size))

        return ResponseEntity.ok(pageQuizResponse)
    }
}