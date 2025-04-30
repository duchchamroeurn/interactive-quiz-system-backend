package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.model.response.PagedResponse
import com.chamroeurn.iqs.model.response.UserResponse
import com.chamroeurn.iqs.model.response.toUserResponse
import com.chamroeurn.iqs.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun allUsers(pageRequest: PageRequest): PagedResponse<UserResponse> {
        val users = userRepository.findAll(pageRequest)

        return PagedResponse(
            data = users.content.map { it.toUserResponse() },
            page = pageRequest.pageNumber,
            size = pageRequest.pageSize,
            totalElements = users.totalElements,
            totalPages = users.totalPages,
            )
    }
}