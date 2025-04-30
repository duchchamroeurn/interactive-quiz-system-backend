package com.chamroeurn.iqs.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException

class RestErrorResponseException(private val problemDetail: ProblemDetail) :
    ErrorResponseException(HttpStatus.valueOf(problemDetail.status), problemDetail, null) {
}