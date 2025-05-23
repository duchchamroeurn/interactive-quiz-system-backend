package com.chamroeurn.iqs.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [OptionsConsistentWithTypeValidator::class])
@Target(AnnotationTarget.CLASS) // Apply to the class
@Retention(AnnotationRetention.RUNTIME)
annotation class OptionsConsistentWithType(
    val message: String = "Options must be empty for True/False or Yes/No questions if customize field in false and have only 2 options if customize true, have at least 3 options for Multiple Choice.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
