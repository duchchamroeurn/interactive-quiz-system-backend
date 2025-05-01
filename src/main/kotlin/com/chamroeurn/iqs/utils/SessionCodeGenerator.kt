package com.chamroeurn.iqs.utils

import org.springframework.stereotype.Component

@Component
class SessionCodeGenerator {
    private val codeLength = 10
    private val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
    private val random = java.util.Random()

    fun generateUniqueSessionCode(): String {
        return (1..codeLength)
            .map { random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}