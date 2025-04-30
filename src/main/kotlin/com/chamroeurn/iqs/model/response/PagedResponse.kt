package com.chamroeurn.iqs.model.response

import java.time.LocalDateTime

data class PagedResponse<T>(
    val success: Boolean = true,
    val data: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
)