package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.response.DropdownResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.service.DropdownService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/dropdown")
class DropdownController(
    private val dropdownService: DropdownService
) {

    @GetMapping("/quizzes")
    fun getQuizzes(
        @RequestParam(
            name = "query",
            defaultValue = ""
        ) query: String,
        @RequestParam(
            name = "size",
            defaultValue = "20"
        ) size: Int,
    ): ResponseEntity<SuccessResponse<List<DropdownResponse>>> {
        val quizDropdownResponse = dropdownService.searchQuizzesDropdown(query, size)
        return ResponseEntity.ok(quizDropdownResponse)
    }
}