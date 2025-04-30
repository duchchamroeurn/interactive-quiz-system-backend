package com.chamroeurn.iqs.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.stream.Collectors
import java.util.stream.Stream

class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, CharSequence> {

    private lateinit var acceptedValues: List<String>

    override fun initialize(annotation: ValueOfEnum) {
        acceptedValues = Stream.of(*annotation.enumClass.java.enumConstants)
            .map { it.name }
            .collect(Collectors.toList())
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        return if (value == null) {
            true
        } else {
            acceptedValues.contains(value.toString().uppercase())
        }
    }
}