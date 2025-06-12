package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.JoinSessionRequest
import com.chamroeurn.iqs.model.request.StartSessionRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.service.SessionService
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
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
@RequestMapping("api/v1/session")
class SessionController(
    private val sessionService: SessionService
) {

    @PostMapping("/start")
    fun startSession(@Valid @RequestBody requestBody: StartSessionRequest): ResponseEntity<SuccessResponse<SessionResponse>> {

        val sessionResponse = sessionService.createNewSession(requestBody)
        return ResponseEntity.ok(sessionResponse)
    }

    @PostMapping("/join")
    fun joinSessionByCode(@Valid @RequestBody requestBody: JoinSessionRequest): ResponseEntity<SuccessResponse<SessionDetailResponse>> {

        val sessionDetailResponse = sessionService.joinSessionByCode(requestBody)
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

    @PostMapping("/delete/{sessionId}")
    fun deleteSession(@PathVariable sessionId: UUID): ResponseEntity<SuccessResponse<String>> {

        val sessionDeleteResponse = sessionService.deleteSession(sessionId)
        return ResponseEntity.ok(sessionDeleteResponse)
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