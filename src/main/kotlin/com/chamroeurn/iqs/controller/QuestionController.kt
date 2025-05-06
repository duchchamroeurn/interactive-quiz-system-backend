package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.CreateQuestionRequest
import com.chamroeurn.iqs.model.request.CreateQuestionWithOptionsRequest
import com.chamroeurn.iqs.model.request.QuestionOptionRequest
import com.chamroeurn.iqs.model.request.UpdateQuestionRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.service.QuestionService
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

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

    @GetMapping
    fun listQuestions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResponse<QuestionResponse>> {

        val questionsResponse = questionService.allQuestions(PageRequest.of(page, size))
        return ResponseEntity.ok(questionsResponse)
    }

    @GetMapping("/{questionId}")
    fun detailQuestion(@PathVariable questionId: UUID): ResponseEntity<SuccessResponse<QuestionWithOptionResponse>> {

        val questionResponse = questionService.viewQuestion(questionId)
        return ResponseEntity.ok(questionResponse)
    }

    @DeleteMapping("/{questionId}")
    fun deleteQuestion(@PathVariable questionId: UUID): ResponseEntity<SuccessResponse<String?>> {
        val message = questionService.deleteQuestion(questionId)
        return ResponseEntity.ok(message)
    }

    @PutMapping("/{questionId}")
    fun updateQuiz(
        @PathVariable questionId: UUID,
        @Valid @RequestBody body: UpdateQuestionRequest
    ): ResponseEntity<SuccessResponse<QuestionResponse>> {
        val quizResponse = questionService.updateQuestion(questionId, body)
        return ResponseEntity.ok(quizResponse)
    }

    @PostMapping("/options/create")
    fun createQuestionWithOptions(@Valid @RequestBody body: QuestionOptionRequest): ResponseEntity<QuestionOptionRequest> {

        return ResponseEntity.ok(body)
    }

}