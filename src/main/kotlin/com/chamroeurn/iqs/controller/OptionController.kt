package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.CreateOptionRequest
import com.chamroeurn.iqs.model.request.UpdateOptionRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.service.OptionService
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("api/v1/option")
class OptionController(
    private val optionService: OptionService
) {

    @PostMapping
    fun createOption(@Valid @RequestBody body: CreateOptionRequest): ResponseEntity<SuccessResponse<OptionResponse>> {
        val optionResponse = optionService.createOption(body)
        return ResponseEntity.ok(optionResponse)
    }

    @PutMapping("/{optionId}")
    fun updateOption(
        @PathVariable optionId: UUID,
        @Valid @RequestBody body: UpdateOptionRequest
    ): ResponseEntity<SuccessResponse<OptionResponse>> {
        val optionResponse = optionService.updateOption(optionId, body)
        return ResponseEntity.ok(optionResponse)
    }

    @GetMapping
    fun listOptions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResponse<OptionResponse>> {

        val pageQuizResponse = optionService.allOptions(PageRequest.of(page, size))
        return ResponseEntity.ok(pageQuizResponse)
    }

    @GetMapping("/{optionId}")
    fun detailQuiz(@PathVariable optionId: UUID): ResponseEntity<SuccessResponse<OptionDetailResponse>> {
        val quizResponse = optionService.viewOption(optionId)
        return ResponseEntity.ok(quizResponse)
    }

    @DeleteMapping("/{optionId}")
    fun deleteOption(@PathVariable optionId: UUID): ResponseEntity<SuccessResponse<String?>> {
        val message = optionService.deleteOption(optionId)
        return ResponseEntity.ok(message)
    }

}