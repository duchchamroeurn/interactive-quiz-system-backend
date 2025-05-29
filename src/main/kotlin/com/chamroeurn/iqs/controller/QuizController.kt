package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.QuizWithQuestionsOptionsRequest
import com.chamroeurn.iqs.model.response.PagedResponse
import com.chamroeurn.iqs.model.response.QuizDetailResponse
import com.chamroeurn.iqs.model.response.QuizResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.service.QuizService
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("api/v1/quiz")
class QuizController(
    private val quizService: QuizService
) {

    @GetMapping("/{quizId}")
    fun detailQuiz(@PathVariable quizId: UUID): ResponseEntity<SuccessResponse<QuizDetailResponse>> {
        val quizResponse = quizService.viewQuiz(quizId)
        return ResponseEntity.ok(quizResponse)
    }

    @DeleteMapping("/{quizId}")
    fun deleteQuiz(@PathVariable quizId: UUID): ResponseEntity<SuccessResponse<String?>> {
        val message = quizService.deleteQuiz(quizId)
        return ResponseEntity.ok(message)
    }

    @GetMapping
    fun listQuizzes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResponse<QuizResponse>> {

        val pageQuizResponse = quizService.allQuizzes(PageRequest.of(page, size))

        return ResponseEntity.ok(pageQuizResponse)
    }

    @PostMapping("/create-with-questions")
    fun createWithQuestions(@Valid @RequestBody body: QuizWithQuestionsOptionsRequest): ResponseEntity<SuccessResponse<QuizResponse>> {
        val quizResponse = quizService.createWithQuestions(body)
        return ResponseEntity.ok(
            quizResponse
        )
    }
}