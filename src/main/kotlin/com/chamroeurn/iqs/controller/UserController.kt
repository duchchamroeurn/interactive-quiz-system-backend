package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.response.PagedResponse
import com.chamroeurn.iqs.model.response.SessionWithQuizResponse
import com.chamroeurn.iqs.model.response.UserResponse
import com.chamroeurn.iqs.repository.entity.SessionAccessType
import com.chamroeurn.iqs.service.SessionService
import com.chamroeurn.iqs.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/v1/user")
class UserController(
    private val userService: UserService,
    private val sessionService: SessionService
) {

    @GetMapping
    fun listUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResponse<UserResponse>> {

        val userResponse = userService.allUsers(PageRequest.of(page, size))
        return ResponseEntity.ok(userResponse)
    }
    @GetMapping("/{userId}/quizzes/available")
    fun getAvailableQuizzesForUser(
        @PathVariable userId: UUID,
        @RequestParam(defaultValue = "PRIVATE") type: SessionAccessType,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "sessionId") sortBy: String, // Sort quizzes by title
        @RequestParam(defaultValue = "ASC") sortDirection: String
    ): ResponseEntity<PagedResponse<SessionWithQuizResponse>> {
        val direction = Sort.Direction.valueOf(sortDirection.uppercase())
        // For PageRequest, the sort order will apply to the SessionEntity fields first,
        // so if you want to sort by quiz properties, make sure they are included in the JPQL
        // or handle sorting of `uniqueQuizzes` list manually before creating PageImpl.
        // For simplicity, we're applying it to the session fetch.
        val pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))

        val quizzesPage = when(type) {
            SessionAccessType.PUBLIC ->  sessionService.getAvailablePublicQuizzesForUser(userId, pageable)
            SessionAccessType.PRIVATE ->  sessionService.getAvailableQuizzesForUser(userId, pageable)
        }
        return ResponseEntity.ok(quizzesPage)
    }
}