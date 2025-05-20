package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.model.response.DropdownResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.model.response.toDropdownResponse
import com.chamroeurn.iqs.repository.QuizRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class DropdownService(
    private val quizRepository: QuizRepository
) {

    fun searchQuizzesDropdown(query: String, size: Int): SuccessResponse<List<DropdownResponse>> {
        val queryResult = quizRepository.findByTitleIgnoreCaseContainingWithLimit(query, PageRequest.ofSize(size))
        return SuccessResponse(
            message = "Quizzes dropdown list",
            data = queryResult.mapNotNull { it.toDropdownResponse() }
        )
    }
}