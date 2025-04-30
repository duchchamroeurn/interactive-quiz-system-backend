package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.response.PagedResponse
import com.chamroeurn.iqs.model.response.UserResponse
import com.chamroeurn.iqs.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun listUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResponse<UserResponse>> {

        val userResponse = userService.allUsers(PageRequest.of(page, size))
        return ResponseEntity.ok(userResponse)
    }
}