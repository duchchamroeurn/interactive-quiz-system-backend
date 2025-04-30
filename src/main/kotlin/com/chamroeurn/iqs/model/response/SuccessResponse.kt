package com.chamroeurn.iqs.model.response

import java.time.LocalDateTime

data class SuccessResponse<T>(
    val success: Boolean = true,
    val data: T? = null,
    val message: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
