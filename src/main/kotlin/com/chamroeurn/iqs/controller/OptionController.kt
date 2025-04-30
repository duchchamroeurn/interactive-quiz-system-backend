package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.CreateOptionRequest
import com.chamroeurn.iqs.model.response.OptionResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.service.OptionService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/option")
class OptionController(
    private val optionService: OptionService
) {

    @PostMapping("/create")
    fun createOption(@Valid @RequestBody body: CreateOptionRequest): ResponseEntity<SuccessResponse<OptionResponse>> {

        val optionResponse = optionService.createOption(body)
        return ResponseEntity.ok(optionResponse)
    }
}