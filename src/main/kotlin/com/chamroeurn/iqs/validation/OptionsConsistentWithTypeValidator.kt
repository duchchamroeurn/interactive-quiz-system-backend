package com.chamroeurn.iqs.validation

import com.chamroeurn.iqs.model.request.QuestionRequest
import com.chamroeurn.iqs.repository.entity.QuestionTypes
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class OptionsConsistentWithTypeValidator: ConstraintValidator<OptionsConsistentWithType, QuestionRequest> {
    override fun isValid(question: QuestionRequest, context: ConstraintValidatorContext?): Boolean {
        return when (question.type) {
            QuestionTypes.TRUE_FALSE.name -> question.options.isNullOrEmpty()
            else -> question.options != null && question.options.size >= 3
        }
    }
}