package com.chamroeurn.iqs.exception

import com.chamroeurn.iqs.model.response.CustomErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime


@RestControllerAdvice
class RestExceptionHandle : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        val errors = mutableMapOf<String, String?>()
        ex.bindingResult.fieldErrors.forEach { error ->
            errors[error.field] = error.defaultMessage
        }

        val errorResponse = CustomErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = ex.body.title,
            message = ex.body.detail,
            fieldErrors = errors
        )

        return ResponseEntity(errorResponse, status)
    }

    override fun handleErrorResponseException(
        ex: ErrorResponseException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val customErrorResponse = CustomErrorResponse(
            status = status.value(),
            error = ex.body.title,
            message = ex.body.detail,
            fieldErrors = ex.body.properties
        )

        return ResponseEntity(customErrorResponse, status)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorResponse = CustomErrorResponse(
            status = status.value(),
            error = HttpStatus.valueOf(status.value()).reasonPhrase,
            message = ex.message,
            fieldErrors = null
        )

        return ResponseEntity(errorResponse, status)
    }

}