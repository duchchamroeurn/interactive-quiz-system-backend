package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.CreateQuestionRequest
import com.chamroeurn.iqs.model.response.QuestionResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.service.QuestionService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/question")
class QuestionController(
    private val questionService: QuestionService
) {
    @PostMapping("/create")
    fun createQuestion(@Valid @RequestBody body: CreateQuestionRequest): ResponseEntity<SuccessResponse<QuestionResponse>> {

        val createQuestionResponse = questionService.createQuestion(body)
        return ResponseEntity.ok(createQuestionResponse)
    }


}