package com.chamroeurn.iqs.model.response

import java.time.LocalDateTime

data class CustomErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String?,
    val message: String?,
    val fieldErrors: Map<String, Any?>?
)